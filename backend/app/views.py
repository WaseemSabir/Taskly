"""
The file implements the view for all the routes in the application.
"""
from app.models import TodoItem, User
from app.persist import TodoRepo, UserRepo
from app import utils
from datetime import datetime
from flask import request
from app.exceptions import *


def login():
    """
    Validates user credentials and returns a JWT token if valid.
    Expects Json Data, Only Post Request allowed, see main.py.
    """
    try:
        data = request.json
        email = data.get("email")
        password = data.get("password")

        try:
            user = UserRepo().filter_by_email(email)
        except NotFound as e:
            return utils.make_general_response(
                success=False, message=e.message, status=404
            )

        if not utils.verify_password(user.password, password):
            return utils.make_general_response(
                success=False, message="Invalid Credentials.", status=401
            )

        data = {"token": utils.generate_jwt_token(user)}
        return utils.make_general_response(data)

    except Exception as e:
        return utils.make_general_response(success=False, message=str(e), status=500)


def register():
    """
    Takes in a user data and creates a new user.
    Expects Json Data. Only Post Request Allowed.
    """
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

        try:
            user = UserRepo().create(user)
        except AlreadyExists as e:
            return utils.make_general_response(
                success=False, message=e.message, status=409
            )

        data = {"token": utils.generate_jwt_token(user)}
        return utils.make_general_response(data)

    except Exception as e:
        return utils.make_general_response(success=False, message=str(e), status=500)


def get_user_view():
    """
    Get user data.
    Only GET method allowed.
    """
    user = utils.get_user_from_request(request)
    if not user:
        return utils.make_general_response(message="Unautorized.", status=401)

    try:
        user = UserRepo().get(id=user.id)
        return utils.make_general_response(data= utils.make_user_dict(user))
    except Exception as e:
        return utils.make_general_response(success=False, message=str(e), status=500)

def update_user_view():
    """
    Updates the user data.
    Expects Json Data In Request.
    Only Put Request Allowed.
    """
    user = utils.get_user_from_request(request)
    if not user:
        return {"error": "Unautorized."}, 401

    try:
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

        try:
            user = UserRepo().update(id=user.id, model=user)
        except AlreadyExists as e:
            return utils.make_general_response(
                success=False, message=e.message, status=409
            )

        return utils.make_general_response(data= utils.make_user_dict(user))
    except Exception as e:
        return utils.make_general_response(success=False, message=str(e), status=500)


def delete_user_view():
    """
    Deletes the user.
    Only DELETE Request Allowed.
    """
    user = utils.get_user_from_request(request)
    if not user:
        return {"error": "Unautorized."}, 401

    try:
        UserRepo().delete(id=user.id)
        return utils.make_general_response(success=True, message="User Deleted.")
    except Exception as e:
        return utils.make_general_response(success=False, message=str(e), status=500)


def user_views():
    """
    Update, get or delete User data.
    """
    if request.method == "GET":
        return get_user_view()
    elif request.method == "PUT":
        return update_user_view()
    elif request.method == "DELETE":
        return delete_user_view()


def create_task():
    """
    Creates a new task.
    Expects Json Data In Request.
    Only POST request allowed, see main.py.
    """
    user = utils.get_user_from_request(request)
    if not user:
        return utils.make_general_response(message="Unautorized.", status=401)

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

        task = TodoRepo().create(task)
        return utils.make_general_response(data=task.__dict__)

    except Exception as e:
        return utils.make_general_response(success=False, message=str(e), status=500)


def task_list():
    """
    Returns all the tasks for the user.
    Only Get Request Allowed. See main.py.
    """
    user = utils.get_user_from_request(request)
    if not user:
        return {"error": "Unautorized."}, 401

    try:
        tasks = TodoRepo().user_tasks_list(user_id=user.id)
        data = [task.__dict__ for task in tasks]
        return utils.make_general_response(data=data)
    except Exception as e:
        return utils.make_general_response(success=False, message=str(e), status=500)


def get_task_view(task_id):
    """
    Get a task.
    Only GET method allowed.
    """
    user = utils.get_user_from_request(request)
    if not user:
        return utils.make_general_response(message="Unautorized.", status=401)

    try:
        try:
            task = TodoRepo().get(id=task_id)
        except NotFound as e:
            return utils.make_general_response(
                success=False, message=e.message, status=404
            )

        if task.user_id != user.id:
            return utils.make_general_response(
                success=False, message="Unautorized.", status=401
            )

        return utils.make_general_response(data=task.__dict__)
    except Exception as e:
        return utils.make_general_response(success=False, message=str(e), status=500)


def update_task_view(task_id):
    """
    Update a task.
    Only PUT method allowed.
    """
    user = utils.get_user_from_request(request)
    if not user:
        return utils.make_general_response(message="Unautorized.", status=401)

    try:
        data = request.json
        title = data.get("title")
        description = data.get("description")

        due = data.get("due_by")
        due_by = datetime.strptime(due, "%Y-%m-%d %H:%M") if due else None
        status = data.get("status")

        try:
            task = TodoRepo().get(id=task_id)
        except NotFound as e:
            return utils.make_general_response(
                success=False, message=e.message, status=404
            )

        if task.user_id != user.id:
            return utils.make_general_response(
                success=False, message="Unautorized.", status=401
            )

        if task.status == "pending" and status == "completed":
            task.completed_at = datetime.now()

        task.title = title if title else task.title
        task.description = description if description else task.description
        task.due_by = due_by if due_by else task.due_by
        task.status = status if status else task.status

        task = TodoRepo().update(id=task.id, model=task)

        return utils.make_general_response(data=task.__dict__)
    except Exception as e:
        print(e)
        return utils.make_general_response(success=False, message=str(e), status=500)


def delete_task_view(task_id):
    """
    Delete a task.
    Only DELETE method allowed.
    """
    user = utils.get_user_from_request(request)
    if not user:
        return utils.make_general_response(message="Unautorized.", status=401)

    try:
        try:
            task = TodoRepo().get(id=task_id)
        except NotFound as e:
            return utils.make_general_response(
                success=False, message=e.message, status=404
            )

        if task.user_id != user.id:
            return utils.make_general_response(
                success=False, message="Unautorized.", status=401
            )

        TodoRepo().delete(id=task.id)
        return utils.make_general_response(success=True, message="Task Deleted.")
    except Exception as e:
        return utils.make_general_response(success=False, message=str(e), status=500)


def task_views(task_id):
    """
    Get, update or delete a task.
    """
    if request.method == "GET":
        return get_task_view(task_id)
    elif request.method == "PUT":
        return update_task_view(task_id)
    elif request.method == "DELETE":
        return delete_task_view(task_id)
