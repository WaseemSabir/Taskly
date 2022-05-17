from dataclasses import dataclass, field
from datetime import datetime
from typing import Literal
import uuid


@dataclass
class User:
    """ Date model for user """

    first_name: str
    last_name: str
    email: str
    password: str
    id: str = field(default_factory=lambda: str(uuid.uuid4()))


@dataclass
class ToDoItem:
    """ Date model representing a todo item """

    title: str
    description: str
    due_by: datetime
    created_by: User
    status: Literal["pending", "completed"] = field(default="pending")
    completed_at: datetime = field(default=None)
    created_at: datetime = field(default_factory=datetime.now)
    id: str = field(default_factory=lambda: str(uuid.uuid4()))

    def __post_init__(self):
        if not self.status in ["pending", "completed"]:
            raise ValueError(
                f"Status must be either pending or completed, not {self.status}"
            )

        if self.status == "completed" and self.completed_at is None:
            self.completed_at = datetime.now()
