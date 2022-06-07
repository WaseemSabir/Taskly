from flask import Flask
from app.utils import wait_for_db
from app import views

app = Flask(__name__)

app.add_url_rule("/register", view_func=views.register, methods=["POST"])
app.add_url_rule("/login", view_func=views.login, methods=["POST"])
app.add_url_rule("/user", view_func=views.user_views, methods=["GET", "PUT", "DELETE"])
app.add_url_rule("/create/task", view_func=views.create_task, methods=["POST"])
app.add_url_rule(
    "/task/<task_id>", view_func=views.task_views, methods=["GET", "PUT", "DELETE"]
)
app.add_url_rule("/tasks", view_func=views.task_list, methods=["GET"])

if __name__ == "__main__":
    wait_for_db()
    app.run(host="0.0.0.0", port=8003, debug=True)
