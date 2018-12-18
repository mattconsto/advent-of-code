use std::io::prelude::*;
use std::fs::File;
use std::sync::{Arc, Mutex};

static HEADER_LEN: usize = 2;

fn visitor(data: &Vec<usize>, ptr: usize, counter: &Arc<Mutex<usize>>) -> usize {
	// Recursively find the length of the children
	let mut total = 0;
	for _i in 0..data[ptr + 0] {
		total += visitor(data, ptr + HEADER_LEN + total, counter);
	}

	// Count up
	let mut counter_deref = counter.lock().unwrap();
	for i in 0..data[ptr + 1] {
		*counter_deref += data[ptr + HEADER_LEN + total + i];
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
	eprintln!("Length: {}", visitor(&numbers, 0, &counter));
	println!("{}", counter.lock().unwrap());
}
