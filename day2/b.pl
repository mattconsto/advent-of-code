#!/usr/bin/env perl

use strict;
use lib '.';
use levenshtein;

# Load input
open my $hdl, '<', 'input.txt';
chomp(my @in = <$hdl>);
close $hdl;

# Find the entry with a levenshtein edit distance of 1
for my $i (0 .. $#in) {
	for my $j (($i+1) .. $#in) {
		my $dist = levenshtein::levenshtein($in[$i], $in[$j]);
		if ($dist <= 3) {
			print "$i/$#in $in[$i] $in[$j] $dist\n";
			if ($dist == 1) {exit;}
		}
	}
}

# Now find the character that has changed manually
