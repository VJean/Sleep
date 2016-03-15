import os
import sys
import re

file = open('sleep','r')
n_line = -1

for line in file:
    # line index for log messages
    n_line = n_line + 1

    # skip comments
    if line.startswith('#'):
        continue

    # 14-3-2016-1-0,14-3-2016-7-25,,true,0
    line_pattern = "^([\d\-]*),([\d\-]*),([\d\-]*),(true|false|),(\d*)$"
    m = re.search('^(.*?),(.*?),(.*?),(.*?),(\d*?)$', line)
    if not m:
        print('line '+str(n_line)+' is not well-formed.')
        continue