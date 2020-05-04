#!/bin/bash
for SCRIPT in tests/cito/
do
if [ -f $SCRIPT -a -x $SCRIPT ]
then
echo $SCRIPT
fi
done