use std::fs;

fn main() {
    let input: Vec<String> = fs::read_to_string("./src/bin/day01.input.txt")
        .unwrap()
        .lines()
        .map(String::from)
        .collect();
    let part1 = input
        .iter()
        .map(|l| get_code_part1(l))
        .map(|s| s.parse::<i32>().unwrap())
        .sum::<i32>();
    println!("Answer Part1: {part1}");
    let part2 = input
        .iter()
        .map(|l| get_code_part2(l))
        .map(|s| s.parse::<i32>().unwrap())
        .sum::<i32>();
    println!("Answer Part2: {part2}");
}

fn get_code_part1(line: &str) -> String {
    let a = line.chars().find(|c| c.is_digit(10)).unwrap();
    let b = line.chars().rev().find(|c| c.is_digit(10)).unwrap();
    return format!("{a}{b}");
}

fn get_code_part2(line: &str) -> String {
    let a = first_digit(&line);
    let b = last_digit(&line);
    return format!("{a}{b}");
}

fn first_digit(line: &str) -> u32 {
    for (i, _c) in line.char_indices() {
        let digit = try_get_digit_at(&line, i);
        if digit.is_some() {
            return digit.unwrap();
        }
    }
    panic!("No digit found in line");
}
fn last_digit(line: &str) -> u32 {
    for (i, _c) in line.char_indices().rev() {
        let digit = try_get_digit_at(&line, i);
        if digit.is_some() {
            return digit.unwrap();
        }
    }
    panic!("No digit found in line");
}

fn try_get_digit_at(line: &str, i: usize) -> Option<u32> {
    let c = line.chars().nth(i).unwrap();
    if c.is_digit(10) {
        return Some(line.chars().nth(i).unwrap().to_digit(10).unwrap());
    }
    return match &line[i..] {
        l if l.starts_with("one") => Some(1),
        l if l.starts_with("two") => Some(2),
        l if l.starts_with("three") => Some(3),
        l if l.starts_with("four") => Some(4),
        l if l.starts_with("five") => Some(5),
        l if l.starts_with("six") => Some(6),
        l if l.starts_with("seven") => Some(7),
        l if l.starts_with("eight") => Some(8),
        l if l.starts_with("nine") => Some(9),
        _ => None,
    };
}
