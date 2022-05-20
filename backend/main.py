from app.utils import wait_for_db
import os

if __name__ == "__main__":
    os.environ["TESTING"] = "True"
    wait_for_db(max_tries=30)
