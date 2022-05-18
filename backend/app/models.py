from dataclasses import dataclass, field
from datetime import datetime
from typing import Literal, Optional


@dataclass
class User:
    """Date model for user"""

    id: str
    first_name: str
    last_name: str
    email: str
    password: str


@dataclass
class TodoItem:
    """Date model representing a todo item"""

    id: str
    title: str
    description: str
    due_by: datetime
    user_id: str
    status: Literal["pending", "completed"] = field(default="pending")
    completed_at: Optional[datetime] = field(default=None)
    created_at: datetime = field(default_factory=datetime.now)

    def __post_init__(self):
        if not self.status in ["pending", "completed"]:
            raise ValueError(
                f"Status must be either pending or completed, not {self.status}"
            )

        if self.status == "completed" and not self.completed_at:
            self.completed_at = datetime.now()

        if self.due_by < datetime.now():
            raise ValueError(f"Due by date must be in the future!")
