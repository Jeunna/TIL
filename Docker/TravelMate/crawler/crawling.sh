#!/bin/sh

python3 -c "import sys; sys.path.append('/crawler/mysql'); from main import update_data; update_data();" > /dev/null
python3 -c "from datetime import date; print('[Crontab] %s, update all restrictions' % (date.today()))"
