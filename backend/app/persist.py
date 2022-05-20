from app.models import User, TodoItem
from app.database import Database


class Repository:
    def __init__(self, tablename):
        self.tablename = tablename

    def get(self, id):
        pass

    def create(self, model):
        pass

    def update(self, id, model):
        pass

    def delete(self, id):
        pass


class UserRepo(Repository):
    def __init__(self):
        super().__init__("user")

    def get(self, id) -> User:
        with Database() as db:
            with db.cursor() as curs:
                sql = f'SELECT * FROM "{self.tablename}" WHERE "id" = %s'
                curs.execute(sql, (id,))
                result = curs.fetchone()
                if result:
                    return User(*result)
                return None

    def create(self, model: User) -> tuple:
        try:
            with Database() as db:
                with db.cursor() as curs:

                    sql = f'INSERT INTO "{self.tablename}" (id, first_name, last_name, email, password) VALUES (%s, %s, %s, %s, %s)'
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

            return self.get(model.id), ""
        except Exception as e:
            return None, str(e).split(":")[1]

    def update(self, id, model: User) -> tuple:
        try:
            with Database() as db:
                with db.cursor() as curs:
                    sql = f'UPDATE "{self.tablename}" SET first_name = %s, last_name = %s, email = %s, password = %s WHERE "id" = %s'
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

            return self.get(id), ""
        except Exception as e:
            return None, str(e).split(":")[1]

    def delete(self, id) -> bool:
        try:
            with Database() as db:
                with db.cursor() as curs:
                    sql = f'DELETE FROM "{self.tablename}" WHERE "id" = %s'
                    curs.execute(sql, (id,))
                    db.commit()

            return True
        except:
            return False

    def filter_by_email(self, email) -> User:
        with Database() as db:
            with db.cursor() as curs:
                sql = f'SELECT * FROM "{self.tablename}" WHERE "email" = %s'
                curs.execute(sql, (email,))
                result = curs.fetchone()
                if result:
                    return User(*result)
                return None


class TodoRepo(Repository):
    def __init__(self):
        super().__init__("todo")

    def get(self, id) -> TodoItem:
        with Database() as db:
            with db.cursor() as curs:

                sql = f"SELECT * FROM {self.tablename} WHERE id = %s"
                curs.execute(sql, (id,))
                result = curs.fetchone()
                if result:
                    return TodoItem(*result)
                return None

    def create(self, model: TodoItem) -> tuple:
        try:
            with Database() as db:
                with db.cursor() as curs:
                    sql = f"INSERT INTO {self.tablename} (id, title, description, due_by, user_id, status, completed_at, created_at) VALUES (%s, %s, %s, %s, %s, %s, %s, %s)"
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

            return self.get(model.id), ""
        except Exception as e:
            return None, str(e).split(":")[1]

    def update(self, id, model: TodoItem) -> tuple:
        try:
            with Database() as db:
                with db.cursor() as curs:
                    sql = f"UPDATE {self.tablename} SET title = %s, description = %s, due_by = %s, user_id = %s, status = %s, completed_at = %s WHERE id = %s"
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

            return self.get(id), ""
        except Exception as e:
            return None, str(e).split(":")[1]

    def delete(self, id) -> bool:
        try:
            with Database() as db:
                with db.cursor() as curs:
                    sql = f"DELETE FROM {self.tablename} WHERE id = %s"
                    curs.execute(sql, (id,))
                    db.commit()

            return True

        except:
            return False

    def user_tasks_list(self, user_id) -> list:
        with Database() as db:
            with db.cursor() as curs:
                sql = f"SELECT * FROM {self.tablename} WHERE user_id = %s"
                curs.execute(sql, (user_id,))
                result = curs.fetchall()
                if result:
                    return [TodoItem(*one) for one in result]

                return []
