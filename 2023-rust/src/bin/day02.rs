fn main() {
    let input = include_str!("day02.input.txt");
    let lines: Vec<&str> = input.split(|c| c == '\n').collect();
    let part1_result: usize = lines
        .iter()
        .enumerate()
        .filter(|(_n, l)| is_possible_game(l))
        .map(|(n, _l)| n + 1)
        .sum();
    println!("Answer Part1: {part1_result}");

    let part2_result: i32 = lines
        .iter()
        .map(|l| game_power(l))
        .sum();
    println!("Answer Part2: {part2_result}");
}

fn is_possible_game(line: &str) -> bool {
    let words: Vec<&str> = line.split_ascii_whitespace().collect();
    for (i, word) in words.iter().enumerate() {
        match word.chars().nth(0).unwrap() {
            'r' => {
                if words[i - 1].parse::<i32>().unwrap() > 12 {
                    return false;
                }
            }
            'g' => {
                if words[i - 1].parse::<i32>().unwrap() > 13 {
                    return false;
                }
            }
            'b' => {
                if words[i - 1].parse::<i32>().unwrap() > 14 {
                    return false;
                }
            }
            _ => {}
        }
    }
    return true;
}

fn game_power(line: &str) -> i32 {
    let mut rgb = (0, 0, 0);
    let words: Vec<&str> = line.split_ascii_whitespace().collect();
    for (i, word) in words.iter().enumerate() {
        match word.chars().nth(0).unwrap() {
            'r' => rgb.0 = rgb.0.max(words[i - 1].parse::<i32>().unwrap()),
            'g' => rgb.1 = rgb.1.max(words[i - 1].parse::<i32>().unwrap()),
            'b' => rgb.2 = rgb.2.max(words[i - 1].parse::<i32>().unwrap()),
            _ => {}
        }
    }
    return rgb.0 * rgb.1 * rgb.2;
}
