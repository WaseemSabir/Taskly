"""
Implements fixtures for tests.
"""
import pytest
from random import choice
from app.models import User, TodoItem
from app.utils import generate_id, random_str
from app.database import Database
from datetime import datetime, timedelta
from app.persist import UserRepo
import os


@pytest.fixture
def user_factory() -> User:
    """
    Generates a random user Object.
    """
    users = [
        User(
            id=generate_id(),
            first_name="John",
            last_name="Doe",
            email=f"john_{random_str(3)}@email.com",
            password=random_str(10),
        ),
        User(
            id=generate_id(),
            first_name="Jane",
            last_name="Kelly",
            email=f"janny_{random_str(3)}@gmail.com",
            password=random_str(10),
        ),
        User(
            id=generate_id(),
            first_name="Waseem",
            last_name="Sabir",
            email=f"waseem_{random_str(3)}@gmail.com",
            password=random_str(10),
        ),
    ]

    return choice(users)


@pytest.fixture
def todo_factory(user_factory) -> TodoItem:
    """
    Generates a random todo item Object.
    Also, for user id, it uses the user_factory fixture and saves the user in database.
    """
    user = user_factory
    UserRepo().create(user)

    todo = TodoItem(
        id=generate_id(),
        title=f"test {random_str(10)}",
        description=f"test {random_str(10)} {random_str(10)}",
        due_by=datetime.now() + timedelta(days=1),
        status="pending",
        user_id=user.id,
    )

    return todo


@pytest.fixture
def set_testing_env() -> None:
    """
    Runs migration on test database and,
    Sets the enviroment for Testing, which is used in Database.py while connecting to DB.
    """
    os.environ["TESTING"] = "True"
    db = Database()
    db.migrate()

    yield
    os.environ["TESTING"] = "False"
