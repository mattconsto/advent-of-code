#!/usr/bin/env perl

use strict;
use lib '.';
use levenshtein;

# Load input
open my $hdl, '<', 'input.txt';
chomp(my @in = <$hdl>);
close $hdl;

# Find the entry with a levenshtein edit distance of 1
my $i = 0, my $j = 0, my $last = 0;
for (; !$last && $i < $#in; $i++) {
	$j = $i + 1;
	for (; !$last && $j < $#in; $j++) {
		my $dist = levenshtein::levenshtein($in[$i], $in[$j]);
		if ($dist <= 3) {
			print STDERR "$i/$#in $in[$i] $in[$j] $dist\n";
			if ($dist == 1) {$last = 1;}
		}
	}
}

# Find the common characters
for my $k (0 .. length($in[$i]) - 1) {
	if(substr($in[$i], $k, 1) eq substr($in[$j], $k, 1)) {
		print substr($in[$i], $k, 1);
	}
}
print "\n";
