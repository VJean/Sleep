import os
import sys
import re
import datetime

def parse_date(s):
    s = str(s)
    match = re.search("^(?P<day>\d+)-(?P<month>\d+)-(?P<year>\d+)-(?P<hour>\d+)-(?P<minute>\d+)$", s)
    result = match.groupdict()
    
    # values must be int
    clean_result = dict()
    clean_result.update((k, int(v)) for k, v in result.items())

    return datetime.datetime(**clean_result)

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
        return "SleepItem#%s, from %s to %s, alone?%s, at:%s" % (self.id, self.begin, self.end, self.alone, self.where)
    def __repr__(self):
        return "<SleepItem#%s;begin:%s;end:%s>" % (self.id, self.begin, self.end)

data_filename = 'data'
file = open(data_filename,'r')
n_line = -1
sleep_items = []

for line in file:
    # line index for log messages
    n_line = n_line + 1

    # skip comments
    if line.startswith('#'):
        continue

    line_pattern = "^([\d\-]+),([\d\-]+),([\d\-]*),(true|false),(\d+)$"
    m = re.search(line_pattern, line)
    # no match : skip
    if not m:
        print('line '+str(n_line)+' could not be parsed.')
        continue

    # match : create item
    m_begin = m.group(1)
    if m_begin :
        m_begin = parse_date(m_begin)
        pass

    m_end = m.group(2)
    if m_end :
        m_end = parse_date(m_end)
        pass

    m_amount = m.group(3)
    if m_amount:
        re_amount = re.search("(?P<hours>\d+)(-(?P<minutes>\d+))?", m_amount).groupdict()
        clean_amount = dict()
        clean_amount.update((k, int(v)) for k, v in re_amount.items() if v is not None)
        m_amount = datetime.timedelta(**clean_amount)

    m_alone = m.group(4)
    if m_alone:
        m_alone = m_alone == "true"
    
    m_where = m.group(5)
    if m_where:
        pass    

    sleep_items.append(SleepItem(m_begin,m_end,m_amount,m_alone,m_where))

print("\ncreated "+str(len(sleep_items))+" entries")
for s in sleep_items:
    print(s)