use std::io::prelude::*;
use std::fs::File;

fn visitor(data: &Vec<usize>, ptr: usize) -> (usize, usize) {
	let (mut len, mut count, mut counts) = (2, 0, Vec::new());

	// Recursively find children
	for _i in 0..data[ptr + 0] {
		let (l, c) = visitor(data, ptr + len);
		len += l;
		counts.push(c);
	}

	// Count up
	if data[ptr + 0] > 0 {
		for i in 0..data[ptr + 1] {
			let p = data[ptr + len + i];
			if p > 0 && p <= counts.len() {count += counts[p - 1];}
		}
	} else {
		for i in 0..data[ptr + 1] {count += data[ptr + len + i];}
	}

	return (len + data[ptr + 1], count);
}

fn main() {
	// Load the file, split by whitespace, and cast to usize
	let mut file  = File::open("input.txt").expect("Cannot open file");
	let mut input = String::new();
	file.read_to_string(&mut input).expect("Cannot read file");
	let numbers = input.split_whitespace().map(|s| s.parse().unwrap()).collect();

	// Display
	let (len, count) = visitor(&numbers, 0);
	eprintln!("Length: {}", len);
	println!("{}", count);
}
