class EnviormentNotSet(Exception):
    """
    Custom exception for checking if Enviorment variable are set.
    """

    def __init__(self, message="Enviorment variables are not set."):
        self.message = message
        super().__init__(self.message)


class AlreadyExists(Exception):
    """
    Exception to raise if there is integrity conflict in database due to inserting duplicate data.
    """

    def __init__(self, key_name):
        self.message = f"{key_name} already exists."
        super().__init__(self.message)


class NotFound(Exception):
    """
    Exception to raise if there is no data found in database.
    """

    def __init__(self, key_name):
        self.message = f"{key_name} not found."
        super().__init__(self.message)
