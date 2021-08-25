import string
import random

class RandomUtil:
    # Random string generator, courtest of Ignacio and Josip
    # Ref: https://stackoverflow.com/questions/2257441/random-string-generation-with-upper-case-letters-and-digits
    @staticmethod
    def randomString(size=10, chars=string.ascii_uppercase + string.digits):
        return ''.join(random.choice(chars) for _ in range(size))