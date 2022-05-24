from app.database import Database
from psycopg2 import OperationalError
import time
from app.models import User
import jwt
import os
from app.persist import UserRepo
import hashlib
import string
import random


def wait_for_db(max_tries: int = 10):
    """Waits for postgres database to connect."""
    retry_count = 0
    max_retry_count = max_tries
    while retry_count < max_retry_count:
        retry_count += 1
        try:
            with Database() as db:
                with db.cursor() as curs:
                    sql = "select 1;"
                    curs.execute(sql)
                    print("*" * 10, "\nDB connected\n", "*" * 10)
                    return
        except OperationalError:
            print("No db")
            if retry_count > max_retry_count:
                raise Exception("DB Failed")
            time.sleep(1)


def generate_id():
    """Generates a random id."""
    import uuid

    return str(uuid.uuid4())


def hash_password(password: str):
    """
    Takes a password and returns a one-way hashed password.
    """
    salt = os.urandom(32)

    key = hashlib.pbkdf2_hmac("sha256", password.encode("utf-8"), salt, 100000)

    return salt + key


def verify_password(store_pass, new_pass: str):
    """
    Takes in password hash and password and verifies if the password is correct.
    """
    store_pass = bytes(store_pass)

    salt = store_pass[:32]
    key = store_pass[32:]

    new_val = hashlib.pbkdf2_hmac("sha256", new_pass.encode("utf-8"), salt, 100000)
    return key == new_val


def random_str(num: int):
    return "".join(random.choices(string.ascii_uppercase + string.digits, k=num))


def generate_jwt_token(user: User):
    """Generate JWT token."""
    payload = {
        "id": user.id,
        "email": user.email,
        "first_name": user.first_name,
        "last_name": user.last_name,
    }
    secret = os.environ.get("JWT_SECRET")
    return jwt.encode(payload, secret, algorithm="HS256")


def verify_jwt_token(token):
    """Verify JWT token."""
    secret = os.environ.get("JWT_SECRET")
    return jwt.decode(token, secret, algorithms=["HS256"])


def get_user_from_request(request):
    """Get user from request."""
    token = request.headers.get("Authorization")
    if token:
        token = token.split(" ")[1]
        payload = verify_jwt_token(token)
        return UserRepo().get(payload["id"])

    return None


def get_general_response(data=[], success=True, message="", status=200):
    return {"success": success, "message": message, "data": data}, status
