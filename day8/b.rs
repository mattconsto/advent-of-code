use std::io::prelude::*;
use std::fs::File;
use std::sync::{Arc, Mutex};

static HEADER_LEN: usize = 2;

// Count the occurrences of a needle in a haystack
fn occurences<T: PartialEq>(needle: T, haystack: &[T]) -> usize {
	return haystack.iter().fold(0, |a,e| if *e == needle {a + 1} else {a});
}

fn visitor(data: &Vec<usize>, ptr: usize, counter: &Arc<Mutex<usize>>, multiplier: usize) -> usize {
	// Recursively find the length of the children
	let mut totals = Vec::new();
	let mut total = 0;
	for _i in 0..data[ptr + 0] {
		totals.push(total);
		total += visitor(data, ptr + HEADER_LEN + total, counter, 0);
	}

	// Count up
	if multiplier > 0 {
		if data[ptr + 0] > 0 {// Recurse
			for i in 0..data[ptr + 0] {
				let offset = ptr + HEADER_LEN + total;
				let occur = occurences(i + 1, &data[offset..(offset + data[ptr + 1])]);
				if occur > 0 {visitor(data, ptr + HEADER_LEN + totals[i], counter, multiplier * occur);}
			}
		} else {// Or add up
			let mut deref = counter.lock().unwrap();
			for i in 0..data[ptr + 1] {*deref += data[ptr + HEADER_LEN + total + i] * multiplier;}
		}
	}

	return HEADER_LEN + total + data[ptr + 1];
}

fn main() {
	// Load the file, split by whitespace, and cast to usize
	let mut file  = File::open("input.txt").expect("Unable to open file");
	let mut input = String::new();
	file.read_to_string(&mut input).expect("Unable to read file");
	let numbers = input.split_whitespace().map(|s| s.parse().unwrap()).collect();

	// Count up 
	let counter = Arc::new(Mutex::new(0));
	eprintln!("Length: {}", visitor(&numbers, 0, &counter, 1));
	println!("{}", counter.lock().unwrap());
}
