import os
import sys
import re
import datetime
import json

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

class Profile(object):
    """Profile class"""
    def __init__(self, name, sleepItems=[]):
        super(Profile, self).__init__()
        self.name = name
        self.sleepItems = sleepItems

def parse_date(s):
    s = str(s)
    match = re.search("^(?P<day>\d+)-(?P<month>\d+)-(?P<year>\d+)-(?P<hour>\d+)-(?P<minute>\d+)$", s)
    result = match.groupdict()
    
    # values must be int
    clean_result = dict()
    clean_result.update((k, int(v)) for k, v in result.items())

    return datetime.datetime(**clean_result)

def encode_json(obj):
    if isinstance(obj, datetime.datetime):
        return obj.isoformat()
    elif isinstance(obj, datetime.timedelta):
        # if obj is empty
        if str(obj) == "" or obj is None:
            return str(obj)

        # convert duration to iso8601 formatting
        t = datetime.datetime.strptime(str(obj), "%H:%M:%S")
        s = "PT{0}H{1}M{2}S".format(t.hour, t.minute, t.second)

        return s
    elif isinstance(obj, (SleepItem, Profile)):
        return obj.__dict__
    else:
        raise TypeError(str(obj.__class__) + "is not serialisable")

if __name__ == "__main__":
    if len(sys.argv) != 3:
         sys.exit("Fatal Error, aborting\n \
             \tusage: to-json.py infile outfile")

    in_filename = sys.argv[1]
    out_filename = sys.argv[2]
    in_file = open(in_filename,'r')
    out_file = open(out_filename,'w')
    
    n_line = -1
    name = ""
    foundName = False
    sleep_items = []

    line_pattern = "^([\d\-]+),([\d\-]+),([\d\-]*),(true|false),(\w+)$"

    for line in in_file:
        # line index for log messages
        n_line = n_line + 1

        # skip comments
        if line.startswith('#'):
            continue

        if not foundName:
            if re.match("^\s*\w+\s*$", line):
                foundName = True
                name = line.strip()
            continue

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

        sleep_items.append(SleepItem(m_begin,m_end,m_amount,m_alone,m_where))


    p = Profile(name,sleep_items)

    print("Parsed profile '"+name+"', "+str(len(sleep_items))+" entries.")
    json.dump(obj=p, fp=out_file, indent=4, default=encode_json)