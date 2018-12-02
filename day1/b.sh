#!/usr/bin/env bash

declare -A seen=()
read -d '' -r -a in < input.txt
echo "${#in[@]} lines loaded, looking for first repeat"
time for ((ptr=freq=0; (seen[$((freq += in[ptr]))] += 1) < 2; ptr = (ptr+1) % ${#in[@]})); do :; done
echo $'\n'"repeated '$freq'"
