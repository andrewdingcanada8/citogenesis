#!/bin/bash
(cd tests/cito/;
for f in *.sh; do
    bash "$f" -H
done)
