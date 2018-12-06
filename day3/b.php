#!/usr/bin/env php
<?php

$lines = explode("\n", trim(file_get_contents('input.txt')));
$claims = [];

$safe = [];

fwrite(STDERR, count($lines) . " claims\n");

for($l = 0; $l < count($lines); $l++) {
	list($id, $x, $y, $w, $h) = sscanf($lines[$l], '#%d @ %d,%d: %dx%d');
	$hit = false;
	for($i = 0; $i < $w; $i++) {
		for($j = 0; $j < $h; $j++) {
			$index = ($x+$i) * 10000 + ($y+$j); # Crude hash
			if(isset($claims[$index])) {
				unset($safe[$claims[$index]]);
				$hit = true;
			} else {
				$claims[$index] = $id;
			}
		}
	}
	if(!$hit) $safe[$id] = true;
}

# No idea, but for some reason this is added.
unset($safe[""]);

echo join(',', array_keys($safe)) . "\n";
