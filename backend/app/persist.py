from abc import abstractmethod, ABC

from psycopg2 import IntegrityError
from app.exceptions import AlreadyExists, NotFound
from app.models import User, TodoItem
from app.database import Database


class Repository(ABC):
    def __init__(self, tablename):
        self.tablename = tablename

    @abstractmethod
    def get(self, id):
        pass

    @abstractmethod
    def create(self, model):
        pass

    @abstractmethod
    def update(self, id, model):
        pass

    @abstractmethod
    def delete(self, id):
        pass


class UserRepo(Repository):
    def __init__(self):
        super().__init__("user")

    def get(self, id) -> User:
        with Database() as db:
            with db.cursor() as curs:
                sql = f'select * from "{self.tablename}" where "id" = %s'
                curs.execute(sql, (id,))
                result = curs.fetchone()
                if result:
                    return User(*result)
                else:
                    raise NotFound(key_name="User")

    def create(self, model: User) -> User:
        try:
            with Database() as db:
                with db.cursor() as curs:

                    sql = f'insert into "{self.tablename}" (id, first_name, last_name, email, password) values (%s, %s, %s, %s, %s)'
                    curs.execute(
                        sql,
                        (
                            model.id,
                            model.first_name,
                            model.last_name,
                            model.email,
                            model.password,
                        ),
                    )
                    db.commit()

            return self.get(model.id)
        except IntegrityError:
            raise AlreadyExists(key_name="User")
        except Exception as e:
            detailStr = str(e).split(":")[1]
            raise Exception(detailStr)

    def update(self, id, model: User) -> User:
        try:
            with Database() as db:
                with db.cursor() as curs:
                    sql = f'update "{self.tablename}" set first_name = %s, last_name = %s, email = %s, password = %s WHERE "id" = %s'
                    curs.execute(
                        sql,
                        (
                            model.first_name,
                            model.last_name,
                            model.email,
                            model.password,
                            id,
                        ),
                    )
                    db.commit()

            return self.get(id)
        except IntegrityError:
            raise AlreadyExists(key_name="User")
        except Exception as e:
            detailStr = str(e).split(":")[1]
            raise Exception(detailStr)

    def delete(self, id):
        with Database() as db:
            with db.cursor() as curs:
                sql = f'delete from "{self.tablename}" where "id" = %s'
                curs.execute(sql, (id,))
                db.commit()

    def filter_by_email(self, email) -> User:
        with Database() as db:
            with db.cursor() as curs:
                sql = f'select id from "{self.tablename}" where "email" = %s'
                curs.execute(sql, (email,))
                result = curs.fetchone()
                if result:
                    return self.get(id=result[0])
                else:
                    raise NotFound(key_name="User with This Email")


class TodoRepo(Repository):
    def __init__(self):
        super().__init__("todo")

    def get(self, id) -> TodoItem:
        with Database() as db:
            with db.cursor() as curs:

                sql = f"select * from {self.tablename} where id = %s"
                curs.execute(sql, (id,))
                result = curs.fetchone()
                if result:
                    return TodoItem(*result)
                else:
                    raise NotFound(key_name="Todo")

    def create(self, model: TodoItem) -> TodoItem:
        try:
            with Database() as db:
                with db.cursor() as curs:
                    sql = f"insert into {self.tablename} (id, title, description, due_by, user_id, status, completed_at, created_at) values (%s, %s, %s, %s, %s, %s, %s, %s)"
                    curs.execute(
                        sql,
                        (
                            model.id,
                            model.title,
                            model.description,
                            model.due_by,
                            model.user_id,
                            model.status,
                            model.completed_at,
                            model.created_at,
                        ),
                    )
                    db.commit()

            return self.get(model.id)
        except IntegrityError:
            raise AlreadyExists(key_name="Todo")
        except Exception as e:
            detailStr = str(e).split(":")[1]
            raise Exception(detailStr)

    def update(self, id, model: TodoItem) -> TodoItem:
        try:
            with Database() as db:
                with db.cursor() as curs:
                    sql = f"update {self.tablename} set title = %s, description = %s, due_by = %s, user_id = %s, status = %s, completed_at = %s where id = %s"
                    curs.execute(
                        sql,
                        (
                            model.title,
                            model.description,
                            model.due_by,
                            model.user_id,
                            model.status,
                            model.completed_at,
                            id,
                        ),
                    )
                    db.commit()

            return self.get(id)
        except IntegrityError:
            raise AlreadyExists(key_name="Todo")
        except Exception as e:
            detailStr = str(e).split(":")[1]
            raise Exception(detailStr)

    def delete(self, id):
        with Database() as db:
            with db.cursor() as curs:
                sql = f"delete from {self.tablename} where id = %s"
                curs.execute(sql, (id,))
                db.commit()

    def user_tasks_list(self, user_id) -> list:
        with Database() as db:
            with db.cursor() as curs:
                sql = f"select id from {self.tablename} where user_id = %s"
                curs.execute(sql, (user_id,))
                result = curs.fetchall()
                if result:
                    return [self.get(one[0]) for one in result]

                return []
