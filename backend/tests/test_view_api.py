"""
Tests all the API views of the application.
"""
from tests.fixtures import *
from flask import json
from app.persist import TodoRepo


def test_register(set_testing_env, client, user_factory):
    """
    Tests the register API.
    """
    user = user_factory
    response = client.post("/register", json=user.__dict__)

    data = json.loads(response.get_data(as_text=True)).get("data")[0]

    assert response.status_code == 200
    assert data.get("token")


def test_login(set_testing_env, client, register_user_factory):
    """
    Tests the login API, depends on Register API.
    """
    user, _ = register_user_factory

    response = client.post(
        "/login", json={"email": user.email, "password": user.password}
    )

    data = json.loads(response.get_data(as_text=True)).get("data")[0]

    assert response.status_code == 200
    assert data.get("token")


def test_duplicate_email_not_allowed(set_testing_env, client, user_factory):
    """
    Tests the register API, with duplicate email.
    """
    user = user_factory
    client.post("/register", json=user.__dict__)

    response = client.post("/register", json=user.__dict__)

    data = json.loads(response.get_data(as_text=True)).get("data")
    msg = json.loads(response.get_data(as_text=True)).get("message")

    assert response.status_code == 400
    assert msg == "User already exists"
    assert not data


def test_get_user_data_api(set_testing_env, client, register_user_factory):
    """
    Tests the get user data API, depends on register API.
    """
    user, token = register_user_factory

    response = client.get("/user", headers={"Authorization": "Bearer " + token})

    data = json.loads(response.get_data(as_text=True)).get("data")[0]

    assert response.status_code == 200
    assert data.get("first_name") == user.first_name
    assert data.get("last_name") == user.last_name
    assert data.get("email") == user.email


def test_update_user_data_api(set_testing_env, client, register_user_factory):
    """
    Tests the update user data API, depends on register API.
    """
    _, token = register_user_factory

    response = client.put(
        "/user",
        headers={"Authorization": "Bearer " + token},
        json={"first_name": "New Name", "last_name": "New Last Name"},
    )

    data = json.loads(response.get_data(as_text=True)).get("data")[0]

    assert response.status_code == 200
    assert data.get("first_name") == "New Name"
    assert data.get("last_name") == "New Last Name"


def test_delete_user_api(set_testing_env, client, register_user_factory):
    """
    Tests the delete user API, depends on register API.
    """
    _, token = register_user_factory

    response = client.delete("/user", headers={"Authorization": "Bearer " + token})

    msg = json.loads(response.get_data(as_text=True)).get("message")

    assert response.status_code == 200
    assert msg == "User Deleted."


def test_create_todo_api(set_testing_env, client, register_user_factory, todo_factory):
    """
    Tests the create todo API.
    """
    user, token = register_user_factory

    todo = todo_factory
    todo.user_id = user.id

    response = client.post(
        "/create/task",
        headers={"Authorization": "Bearer " + token},
        json={
            "title": todo.title,
            "description": todo.description,
            "due_by": todo.due_by.strftime("%Y-%m-%d %H:%M"),
        },
    )

    data = json.loads(response.get_data(as_text=True)).get("data")[0]

    assert response.status_code == 200
    assert data.get("title") == todo.title
    assert data.get("description") == todo.description


def test_update_todo_api(set_testing_env, client, register_user_factory, todo_factory):
    """
    Tests the update todo API.
    """
    user, token = register_user_factory

    todo = todo_factory
    todo.user_id = user.id

    user = UserRepo().get(id=user.id)

    todo_repo = TodoRepo()
    todo_repo.create(todo)

    response = client.put(
        f"/task/{todo.id}",
        headers={"Authorization": "Bearer " + token},
        json={
            "title": "New Title",
            "description": "New Description",
        },
    )

    data = json.loads(response.get_data(as_text=True)).get("data")[0]

    assert response.status_code == 200
    assert data.get("title") == "New Title"
    assert data.get("description") == "New Description"


def test_delete_todo_api(set_testing_env, client, register_user_factory, todo_factory):
    """
    Tests the delete todo API.
    """
    user, token = register_user_factory

    todo = todo_factory
    todo.user_id = user.id

    user = UserRepo().get(id=user.id)

    todo_repo = TodoRepo()
    todo_repo.create(todo)

    response = client.delete(
        f"/task/{todo.id}", headers={"Authorization": "Bearer " + token}
    )

    msg = json.loads(response.get_data(as_text=True)).get("message")

    assert response.status_code == 200
    assert msg == "Task Deleted."

    still_exist = todo_repo.get(id=todo.id)
    assert not still_exist


def test_task_list_api(set_testing_env, client, register_user_factory, todo_factory):
    """
    Tests the task list API.
    """
    user, token = register_user_factory

    todo = todo_factory
    todo.user_id = user.id

    user = UserRepo().get(id=user.id)

    todo_repo = TodoRepo()
    todo_repo.create(todo)

    response = client.get("/tasks", headers={"Authorization": "Bearer " + token})

    data = json.loads(response.get_data(as_text=True)).get("data")

    assert response.status_code == 200
    assert type(data) == list
    assert data[0].get("title") == todo.title
    assert data[0].get("description") == todo.description
