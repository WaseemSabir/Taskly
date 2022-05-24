"""
The file implements the view for all the routes in the application.
"""
from app.models import TodoItem, User
from app.persist import TodoRepo, UserRepo
from app import utils
from datetime import datetime
from flask import request


def login():
    """
    Validates user credentials and returns a JWT token if valid.
    Expects Json Data.
    """
    if request.method == "POST":
        try:
            data = request.json
            email = data.get("email")
            password = data.get("password")

            user = UserRepo().filter_by_email(email)

            if user and utils.verify_password(user.password, password):
                data = {"token": utils.generate_jwt_token(user)}
                return utils.get_general_response(data)

            else:
                return utils.get_general_response(
                    success=False, message="Invalid Credentials.", status=401
                )
        except Exception as e:
            return utils.get_general_response(success=False, message=str(e), status=500)


def register():
    """
    Takes in a user data and creates a new user.
    Expects Json Data.
    """
    if request.method == "POST":
        try:
            data = request.json
            first_name = data.get("first_name")
            last_name = data.get("last_name")
            email = data.get("email")
            password = utils.hash_password(data.get("password"))

            user = User(
                id=utils.generate_id(),
                first_name=first_name,
                last_name=last_name,
                email=email,
                password=password,
            )
            user, err = UserRepo().create(user)

            if user:
                data = {"token": utils.generate_jwt_token(user)}
                return utils.get_general_response(data)
            else:
                return utils.get_general_response(
                    success=False, message=err, status=400
                )
        except Exception as e:
            return utils.get_general_response(success=False, message=str(e), status=500)


def user_views():
    """
    Update, get or delete User data.
    """
    user = utils.get_user_from_request(request)
    if not user:
        return {"error": "Unautorized."}, 401

    try:
        if request.method == "PUT":
            data = request.json
            first_name = data.get("first_name", None)
            last_name = data.get("last_name", None)
            email = data.get("email", None)

            password = data.get("password", None)
            password = utils.hash_password(password) if password else None

            user.first_name = first_name if first_name else user.first_name
            user.last_name = last_name if last_name else user.last_name
            user.email = email if email else user.email
            user.password = password if password else user.password

            user, err = UserRepo().update(id=user.id, model=user)

            if not user:
                return utils.get_general_response(
                    success=False, message=err, status=400
                )

        if request.method == "DELETE":
            UserRepo().delete(id=user.id)
            return utils.get_general_response(success=True, message="User Deleted.")

        data = {
            "id": user.id,
            "first_name": user.first_name,
            "last_name": user.last_name,
            "email": user.email,
        }
        return utils.get_general_response(data)
    except Exception as e:
        return utils.get_general_response(success=False, message=str(e), status=500)


def create_task():
    """
    Creates a new task.
    Expects Json Data In Request.
    """
    if request.method == "POST":
        user = utils.get_user_from_request(request)
        if not user:
            return utils.get_general_response(message="Unautorized.", status=401)

        try:
            data = request.json
            title = data.get("title")
            description = data.get("description")
            due_by = datetime.strptime(data.get("due_by"), "%Y-%m-%d %H:%M")

            task = TodoItem(
                id=utils.generate_id(),
                title=title,
                description=description,
                user_id=user.id,
                due_by=due_by,
                status="pending",
            )
            task, err = TodoRepo().create(task)

            if task:
                return utils.get_general_response(data=task.__dict__)
            else:
                return utils.get_general_response(
                    success=False, message=err, status=400
                )
        except Exception as e:
            return utils.get_general_response(success=False, message=str(e), status=500)


def task_list():
    """
    Returns all the tasks for the user.
    """
    user = utils.get_user_from_request(request)
    if not user:
        return {"error": "Unautorized."}, 401

    try:
        tasks = TodoRepo().user_tasks_list(user_id=user.id)
        data = [task.__dict__ for task in tasks]
        return utils.get_general_response(data=data)
    except Exception as e:
        return utils.get_general_response(success=False, message=str(e), status=500)


def task_views(task_id):
    """
    Get, update or delete a task.
    """
    user = utils.get_user_from_request(request)
    if not user:
        return utils.get_general_response(message="Unautorized.", status=401)

    try:
        task = TodoRepo().get(id=task_id)
        if not task:
            return utils.get_general_response(message="Task Not Found.", status=404)

        if task.user_id != user.id:
            return utils.get_general_response(
                success=False, message="Unautorized.", status=401
            )

        if request.method == "PUT":
            data = request.json
            title = data.get("title", None)
            description = data.get("description", None)
            due = data.get("due_by", None)
            due_by = datetime.strptime(due, "%b %d %Y %I:%M%p") if due else None
            status = data.get("status", None)

            if task.status == "pending" and status == "completed":
                task.completed_at = datetime.now()

            task.title = title if title else task.title
            task.description = description if description else task.description
            task.due_by = due_by if due_by else task.due_by
            task.status = status if status else task.status

            task, err = TodoRepo().update(id=task.id, model=task)

            if not task:
                return utils.get_general_response(
                    success=False, message=err, status=400
                )

        if request.method == "DELETE":
            TodoRepo().delete(id=task.id)
            return utils.get_general_response(success=True, message="Task Deleted.")

        return utils.get_general_response(data=task.__dict__)
    except Exception as e:
        return utils.get_general_response(success=False, message=str(e), status=500)
