"""
Implements the tests for data models of application
"""
from app.models import User, ToDoItem
from datetime import datetime, timedelta
import pytest


def test_user_model():
    """
    Checks the validity of the user model
    """
    user = User(
        first_name="waseem",
        last_name="sabir",
        email="whatever@gmail.com",
        password="hello@world",
    )

    assert user.first_name == "waseem"
    assert user.last_name == "sabir"
    assert user.email == "whatever@gmail.com"
    assert user.password == "hello@world"
    assert user.id is not None
    assert isinstance(user.id, str)


def test_default_todo_model():
    """
    Checks the validity of the todo model with default values
    """
    user = User(
        first_name="waseem",
        last_name="sabir",
        email="whatever@gmail.com",
        password="hello@world",
    )

    todo = ToDoItem(
        title="test",
        description="test",
        due_by=datetime.now() + timedelta(days=1),
        created_by=user,
    )

    tommorow = datetime.now() + timedelta(days=1)
    today = datetime.now()

    assert todo.title == "test"
    assert todo.description == "test"
    assert todo.due_by.date() == tommorow.date()
    assert todo.created_by == user
    assert todo.created_at.date() == today.date()
    assert todo.id is not None
    assert isinstance(todo.id, str)
    assert todo.status == "pending"
    assert todo.completed_at is None


def test_invalid_todo_status():
    """
    Checks if todo model raises error when invalid status is passed
    """
    user = User(
        first_name="waseem",
        last_name="sabir",
        email="whatever@gmail.com",
        password="hello@world",
    )

    with pytest.raises(ValueError):
        todo = ToDoItem(
            title="test",
            description="test",
            due_by=datetime.now() + timedelta(days=1),
            created_by=user,
            status="invalid",
        )


def test_complete_todo_status():
    """
    Checks if todo model sets the completed at date when status is changed to completed
    """
    user = User(
        first_name="waseem",
        last_name="sabir",
        email="whatever@gmail.com",
        password="hello@world",
    )

    todo = ToDoItem(
        title="test",
        description="test",
        due_by=datetime.now() + timedelta(days=1),
        created_by=user,
        status="completed",
    )

    today = datetime.now()
    assert todo.status == "completed"
    assert todo.completed_at.date() == today.date()


def test_completed_at_is_not_overwritten():
    """
    Checks that the todo model does not overwrite the completed_at date
    """
    user = User(
        first_name="waseem",
        last_name="sabir",
        email="whatever@gmail.com",
        password="hello@world",
    )

    todo = ToDoItem(
        title="test",
        description="test",
        due_by=datetime.now() + timedelta(days=1),
        created_by=user,
        status="completed",
        completed_at=datetime.now() - timedelta(days=1),
    )

    assert todo.completed_at.date() == (datetime.now() - timedelta(days=1)).date()
