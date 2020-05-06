#!/bin/bash
test_name=`basename $0`
echo "Running $test_name..."

CMD="gen \"http://localhost:63342/term-project-ading6-cshi18-jgong15-sshaw4-1/dummies/convergent.html\" 60 \"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\""
EXPECT="http://localhost:63342/term-project-ading6-cshi18-jgong15-sshaw4-1/dummies/singleton.html"

OUT=`(sleep 2; echo $CMD) | (cd ../../ ; ./run)`

if [[ "$OUT" == "$EXPECT" ]]; then
  echo -e "\e[32m$test_name PASSED\e[0m"
else
  echo -e "\e[31m$test_name FAILED\e[0m"
  echo "EXPECTED: $EXPECT"
  echo "RECEIVED: $OUT"
fi
