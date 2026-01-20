from collections.abc import MutableMapping


class DictProxy(dict):
    def __init__(self, raw: MutableMapping):
        self.raw = raw

    def __setitem__(self, key, value):
        return self.raw.__setitem__(key, value)

    def __getitem__(self, key):
        return self.raw.__getitem__(key)
