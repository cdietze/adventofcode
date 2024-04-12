use std::fs;

fn main() {
    let list: Vec<String> = fs::read_to_string("./src/bin/day01.input.txt")
        .unwrap()
        .lines()
        .map(String::from)
        .map(get_code)
        .collect();

    let sum = list.iter().map(|s| s.parse::<i32>().unwrap()).sum::<i32>();
    println!("Answer: {sum}");
}

fn get_code(line: String) -> String {
    let a = line.chars().find(|c| c.is_digit(10)).unwrap();
    let b = line.chars().rev().find(|c| c.is_digit(10)).unwrap();
    return format!("{a}{b}");
}
