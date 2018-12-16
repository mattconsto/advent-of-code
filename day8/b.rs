use std::io::prelude::*;
use std::fs::File;
use std::sync::{Arc, Mutex};

static HEADER_L: usize = 2;

fn node_length(data: &Vec<usize>, ptr: usize, count: &Arc<Mutex<usize>>) -> usize {
	// Find lengths
	let children_l = children_length(data, ptr, count);
	let metadata_l = data[ptr + 1];

	// Count up
	let mut count_deref = count.lock().unwrap();
	for i in 0..metadata_l {*count_deref += data[ptr + HEADER_L + children_l + i];}

	return HEADER_L + children_l + metadata_l;
}

fn children_length(data: &Vec<usize>, ptr: usize, count: &Arc<Mutex<usize>>) -> usize {
	// Recursively find the length of the children
	let mut total: usize = 0;
	for _i in 0..data[ptr + 0] {total += node_length(data, ptr + HEADER_L + total, count);}
	return total;
}

fn main() {
	// Load the file, split by whitespace, and cast to usize
	let mut file  = File::open("input.txt").expect("Unable to open the file");
	let mut input = String::new();
	file.read_to_string(&mut input).expect("Unable to read the file");
	let numbers: Vec<usize> = input.split_whitespace().map(|s| s.parse().unwrap()).collect();

	// Count up 
	let count = Arc::new(Mutex::new(0usize));
	eprintln!("Length: {}", node_length(&numbers, 0, &count));
	println!("{}", count.lock().unwrap());
}
