import os
import sys
import re
import datetime

data_filename = 'data'


class SleepItem(object):
    """SleepItem class"""
    item_counter = 0
    def __init__(self, begin=None, end=None, amount=None, alone=None, where=None):
        super(SleepItem, self).__init__()
        # generate unique id
        self.__class__.item_counter += 1
        self.id = self.__class__.item_counter
        self.begin = begin
        self.end = end
        self.amount = amount
        self.alone = alone
        self.where = where
    def __str__(self):
        return "SleepItem#%s, from %s to %s" % (self.id, self.begin, self.end)
    def __repr__(self):
        return "<SleepItem#%s;begin:%s;end:%s>" % (self.id, self.begin, self.end)

file = open(data_filename,'r')
n_line = -1
sleep_items = []

for line in file:
    # line index for log messages
    n_line = n_line + 1

    # skip comments
    if line.startswith('#'):
        continue

    line_pattern = "^([\d\-]*),([\d\-]*),([\d\-]*),(true|false|),(\d*)$"
    m = re.search('^(.*?),(.*?),(.*?),(.*?),(\d*?)$', line)
    # no match : skip
    if not m:
        print('line '+str(n_line)+' is not well-formed.')
        continue

    # match : create item
    m_begin = m.group(1)
    m_end = m.group(2)
    m_amount = m.group(3)
    m_alone = m.group(4)
    m_where = m.group(5)
    sleep_items.append(SleepItem(m_begin,m_end,m_amount,m_where,m_alone))


print("\ncreated "+str(len(sleep_items))+" entries")
