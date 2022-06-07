"""
Implements default database connection class.
"""
import os
from psycopg2 import connect as db_connect
from app.exceptions import EnviormentNotSet

class Database:
    """
    Database class.

    Source:
    Some of code take from Tajir Python Flex repo.
    """

    def __init__(self):
        """Initialize database."""
        self._conn = None
        self.connect()

    def __enter__(self):
        return self

    def __exit__(self, typ, value, traceback):
        try:
            if typ or value or traceback:
                self.rollback()
            else:
                self.commit()

        finally:
            self.close()

    @staticmethod
    def _get_args() -> dict:
        """Get database connection arguments from enviorment."""
        data = {
            "dbname": os.environ.get("POSTGRES_DB_NAME"),
            "user": os.environ.get("POSTGRES_USER"),
            "password": os.environ.get("POSTGRES_PASSWORD"),
            "host": os.environ.get("POSTGRES_HOST"),
            "port": os.environ.get("POSTGRES_PORT"),
            "connect_timeout": os.environ.get("POSTGRES_CONNECTION_TIMEOUT"),
        }

        if not all(data.values()):
            raise EnviormentNotSet

        return data

    @staticmethod
    def _get_test_args() -> dict:
        """Get TEST database connection arguments from enviorment."""
        data = {
            "dbname": os.environ.get("POSTGRES_DB_NAME_TEST"),
            "user": os.environ.get("POSTGRES_USER_TEST"),
            "password": os.environ.get("POSTGRES_PASSWORD_TEST"),
            "host": os.environ.get("POSTGRES_HOST_TEST"),
            "port": os.environ.get("POSTGRES_PORT_TEST"),
            "connect_timeout": os.environ.get("POSTGRES_CONNECTION_TIMEOUT_TEST"),
        }

        if not all(data.values()):
            raise EnviormentNotSet

        return data

    def _get_args_by_env(self) -> dict:
        # Checks if Enviorment is testing or not, Used in set_env fixture in testing
        test = os.environ.get("TESTING")
        if test and test.lower() == "true":
            args = self._get_test_args()
        else:
            args = self._get_args()
        return args

    def connect(self):
        """Connect to database."""
        args = self._get_args_by_env()
        self._conn = db_connect(**args)

    def close(self):
        """Close database connection."""
        self._conn.close()

    def commit(self):
        """Commit transaction."""
        self._conn.commit()

    def rollback(self):
        """Rollback transaction."""
        self._conn.rollback()

    def cursor(self, *args, **kwargs):
        """Get database cursor."""
        return self._conn.cursor(*args, **kwargs)

    def execute(self, query, *args, **kwargs):
        """Execute query and returns cursor."""
        cursor = self.cursor()
        cursor.execute(query, *args, **kwargs)
        return cursor

    def migrate(self):
        """
        Run migrations.
        """
        cur_dir = os.getcwd()

        # if current directory is tests
        if cur_dir.split("/")[-1] == "tests":
            os.chdir("..")

        # go into migrations directory
        if "migrations" in os.listdir():
            os.chdir("migrations")

        # get enviorment variables and run migrations
        args = self._get_args_by_env()
        os.system(
            f'pg-migrator postgres://{args.get("user")}:{args.get("password")}@{args.get("host")}:{args.get("port")}/{args.get("dbname")} > /dev/null'
        )

        os.chdir(cur_dir)

    def truncate(self, table_name):
        """Truncate table."""
        self.execute(f'TRUNCATE TABLE "{table_name}" RESTART IDENTITY CASCADE;')
