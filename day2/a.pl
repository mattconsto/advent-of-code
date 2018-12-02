#!/usr/bin/env perl

use strict;

# Unique subroutine
sub uniq {my %seen; return grep {!$seen{$_}++} @_;}

# Load input
open my $hdl, '<', 'input.txt';
chomp(my @in = <$hdl>);
close $hdl;

# Count letters, get unique counts, then add them to repeats
my @reps = ();
for my $i (0 .. $#in) {
	my @counts = ();
	for my $c (split //, $in[$i]) {$counts[ord $c] += 1;}
	@counts = uniq(@counts);
	for my $j (0 .. $#counts) {$reps[$counts[$j]] += 1;}
}

print $reps[2] * $reps[3] . "\n";
