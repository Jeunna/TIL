#!/bin/sh

python3 -c "import sys; sys.path.append('/crawler'); import crawler; print(crawler.get_korea_restriction())"
