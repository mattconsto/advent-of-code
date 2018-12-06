#!/usr/bin/env php
<?php

$lines = explode("\n", trim(file_get_contents('input.txt')));
$claims = [];

fwrite(STDERR, count($lines) . " claims\n");

for($l = 0; $l < count($lines); $l++) {
	list($id, $x, $y, $w, $h) = sscanf($lines[$l], '#%d @ %d,%d: %dx%d');
	for($i = 0; $i < $w; $i++) {
		for($j = 0; $j < $h; $j++) {
			$index = ($x+$i) * 10000 + ($y+$j); # Crude hash
			$claims[$index] = (isset($claims[$index]) ? $claims[$index] : 0) + 1;
		}
	}
}

# Count overlapping claims.
echo count(array_filter($claims, function($v) {return $v > 1;})) . "\n";
