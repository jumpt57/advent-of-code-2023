read_my_file = filename -> split(read(open(filename, "r"), String), "\n\n")
extract_seeds = file -> split(strip(last(split(first(filter(line -> contains(line, "seeds"), file)), ':'))), ' ')
to_int = str -> parse(Int64, str)

function extract_next_value(line, previous_value)

    next_value = previous_value

    numbers_with_label = split(line, "\n")
    popfirst!(numbers_with_label)

    for values in numbers_with_label
        splitted = split(values, ' ')
        destination = to_int(popfirst!(splitted))
        source = to_int(popfirst!(splitted))
        range = to_int(popfirst!(splitted))
        if source <= previous_value <= source + range
            soil_value = previous_value + (destination - source)
            next_value = soil_value
        end
    end

    return next_value
end

extract = (label, last_value) -> first(map(line -> extract_next_value(line, last_value), filter(line -> contains(line, label), lines)))

lines = read_my_file("full.txt")

locations = Int[]

for seed in extract_seeds(lines)

    seed = to_int(seed)

    println("Seed to find $(seed)") 

    soil_value = extract("seed-to-soil", seed)
    fertilizer_value = extract("soil-to-fertilizer", soil_value)
    water_value = extract("fertilizer-to-water", fertilizer_value)
    light_value = extract("water-to-light", water_value)
    temperature_value = extract("light-to-temperature", light_value)
    humidity_value = extract("temperature-to-humidity", temperature_value) 
    location_value = extract("humidity-to-location", humidity_value) 

    push!(locations, location_value)

    println("--------") 
end

minimum_location = minimum(locations)

println("Lowest location value is $(minimum_location)")