read_my_file = filename -> split(read(open(filename, "r"), String), "\n\n")
to_int = str -> parse(Int64, str)

function extract_previous_value(ranges, next_value)

    previous_value = next_value

    for range in ranges
        destination = range[1]
        source = range[2]
        range = range[3]
        if destination <= next_value <= destination + range
            previous_value = next_value  + (source - destination)
            break
        end
    end

    return previous_value
end

function extract_map(block) 
    lines = split(block, "\n")
    popfirst!(lines)

    array = map(line -> map(str -> to_int(str), split(line, " ")), lines)

    return array
end

function generate_seeds_ranges(seeds_range)
    local seeds = Tuple{Int64, Int64}[]
    i = 1
    while i <= length(seeds_range)
        seed_start = seeds_range[i]
        range = seeds_range[i+1]
        push!(seeds, (seed_start, range))
        i += 2
    end
    sort!(seeds)
    return seeds
end

function from_location_to_seed(maps, location)

    humidity = extract_previous_value(maps["humidity-to-location"], location) 
    temperature = extract_previous_value(maps["temperature-to-humidity"], humidity) 
    light = extract_previous_value(maps["light-to-temperature"], temperature)
    water = extract_previous_value(maps["water-to-light"], light)
    fertilizer = extract_previous_value(maps["fertilizer-to-water"], water)
    soil = extract_previous_value(maps["soil-to-fertilizer"], fertilizer) 

    return extract_previous_value(maps["seed-to-soil"], soil) 
end

function extract_maps(blocks)

    values =  Dict([])

    for block in blocks

        if contains(block, "seeds:")
            parts = split(block, ":")
            popfirst!(parts)
            numbers = strip(first(parts))
            values["seeds"] = map(chunk -> to_int(chunk), split(numbers, " "))
            continue

        elseif contains(block, "seed-to-soil")
            values["seed-to-soil"] = extract_map(block)
            continue

        elseif contains(block, "soil-to-fertilizer")
            values["soil-to-fertilizer"] = extract_map(block)
            continue

        elseif contains(block, "fertilizer-to-water")
            values["fertilizer-to-water"] = extract_map(block)
            continue

        elseif contains(block, "water-to-light")
            values["water-to-light"] = extract_map(block)
            continue

        elseif contains(block, "light-to-temperature")
            values["light-to-temperature"] =extract_map(block)
            continue

        elseif contains(block, "temperature-to-humidity")
            values["temperature-to-humidity"] = extract_map(block)
            continue

        elseif contains(block, "humidity-to-location")
            values["humidity-to-location"] = extract_map(block)
            continue

        else
            continue
        end
    end

    return values
end

function main()

    local blocks = read_my_file("full.txt")
    local maps = extract_maps(blocks)
    local seed_ranges = generate_seeds_ranges(maps["seeds"])

    location = 0
    while true

        println(location)

        seed = from_location_to_seed(maps, location)
        if length(filter(range -> range[1] <= seed <= range[1] + range[2] - 1, seed_ranges)) >= 1
            break
        end

        location += 1
    end

end

println("Got $(Threads.nthreads()) threads")
@time main()