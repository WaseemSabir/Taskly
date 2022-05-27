"""
Implements the tests for all persistence classes.
"""
from app.persist import TodoRepo, UserRepo
from app.utils import verify_password, hash_password
from tests.fixtures import *
from app.database import Database
from app.exceptions import *


def test_env_settings():
    """
    tests if enviorment is set properly
    """
    try:
        db_config = Database._get_args()
        test_config = Database._get_test_args()
    except EnviormentNotSet as e:
        assert False, e.message


def test_user_creation(set_testing_env, user_factory):
    """
    Tests the user creation feature in User Repo.
    """
    user = user_factory
    user_repo = UserRepo()

    try:
        user_repo.create(user)
    except AlreadyExists:
        pass

    try:
        user_from_db = user_repo.get(user.id)
    except NotFound as e:
        assert False, e.message

    assert user_from_db.first_name == user.first_name
    assert user_from_db.last_name == user.last_name
    assert user_from_db.email == user.email


def test_user_password_hashing(set_testing_env, user_factory):
    """
    Checks the validity of the user password hashing.
    """
    user = user_factory
    user_pass = user.password
    user.password = hash_password(user_pass)
    user_repo = UserRepo()
    user_repo.create(user)

    user_from_db = user_repo.get(user.id)
    assert verify_password(user_from_db.password, user_pass)


def test_user_update(set_testing_env, user_factory):
    """
    Tests User Update flow in User Repo.
    """
    user = user_factory
    user_repo = UserRepo()
    user_repo.create(user)

    new_user = user
    new_user.first_name = "New Name"
    user_repo.update(user.id, new_user)

    user_from_db = user_repo.get(user.id)
    assert user_from_db.first_name == new_user.first_name


def test_user_deletion(set_testing_env, user_factory):
    """
    Tests User deletion flow in User Repo.
    """
    user = user_factory
    user_repo = UserRepo()
    user_repo.create(user)

    try:
        user_repo.delete(user.id)
    except:
        assert False, "User deletion failed"

    with pytest.raises(NotFound):
        user_repo.get(user.id)


def test_filter_by_email(set_testing_env, user_factory):
    """
    Tests the filter by email feature in User Repo.
    """
    user = user_factory
    user_repo = UserRepo()
    user_repo.create(user)
    try:
        user_from_db = user_repo.filter_by_email(user.email)
    except NotFound:
        assert False, "User not found"
    
    assert user_from_db.first_name == user.first_name
    assert user_from_db.last_name == user.last_name
    assert user_from_db.email == user.email
    

def test_todo_creation(set_testing_env, todo_factory):
    """
    Tests the todo creation feature in Todo Repo.
    """
    todo = todo_factory
    todo_repo = TodoRepo()
    todo_repo.create(todo)

    try:
        todo_from_db = todo_repo.get(todo.id)
    except NotFound as e:
        assert False, e.message

    assert todo_from_db.title == todo.title
    assert todo_from_db.description == todo.description
    assert todo_from_db.status == todo.status


def test_todo_update(set_testing_env, todo_factory):
    """
    Tests Todo Update flow in Todo Repo.
    """
    todo = todo_factory
    todo_repo = TodoRepo()
    todo_repo.create(todo)

    new_todo = todo
    new_todo.title = "New Title"
    todo_repo.update(todo.id, new_todo)

    todo_from_db = todo_repo.get(todo.id)
    assert todo_from_db.title == new_todo.title


def test_todo_deletion(set_testing_env, todo_factory):
    """
    Tests Todo deletion flow in Todo Repo.
    """
    todo = todo_factory
    todo_repo = TodoRepo()
    todo_repo.create(todo)

    try:
        todo_repo.delete(todo.id)
    except:
        assert False, "Todo deletion failed"
    
    with pytest.raises(NotFound):
        todo_repo.get(todo.id)


def test_todo_user_task_list(set_testing_env, todo_factory):
    """
    Tests the todo user task list feature in Todo Repo.
    """
    todo = todo_factory
    todo_repo = TodoRepo()
    todo_repo.create(todo)

    try:
        data = todo_repo.user_tasks_list(todo.user_id)
    except NotFound as e:
        assert False, e.message

    todo_from_db = data[0]
    assert todo_from_db.title == todo.title
    assert todo_from_db.description == todo.description
    assert todo_from_db.status == todo.status
