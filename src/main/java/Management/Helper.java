package Management;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class Helper {
    public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    public static final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final String standardhelp = """
                                               Use one of the following commands:
                                               new {book-name} [read-to] [link] [abbreviations...]
                                               read {book} {pages-read}
                                               read-to {book} {page-read-to}
                                               list [type]
                                               list-all
                                               show {book} [type]
                                               change {type} {book} {new-value}
                                               add abbreviation {book} {new-value}
                                               help (or h)
                                                                                              
                                               secret {command} [parameters]
                                                       
                                               For details use "help {command}"
                                               """;

    private static final String newhelp = """
                                          Adds a new book with given parameters to the list.
                                                                                    
                                          How to use command:
                                          new + {book-name} + [page] + [link] + [writing-status] + [last-read] + [abbreviations...]
                                                                                     
                                          E.g.  new "Solo Leveling"
                                                new "Tales of Demons and Gods" 358 www.somelink.com/todag
                                                new "The Beginning after The End" 164 null TBATE tbate "Arthur Leywin"
                                                                                    
                                          [page] = page or chapter you have read to
                                          [link] = online link to e-books
                                          [writing-status] ∈ {ComingUp, Rolling, Paused, Ended}
                                          [last-read] = last time the book was read (default:now)
                                          [abbreviations...] = multiple abbreviations or abbreviations of book-name
                                                          listed as multiple whitespace seperated words
                                                                                     
                                          [...] are optional values
                                          But if you decide to not use an optional value but the last one, you have to fill it with something like null or 0.
                                          """;

    private static final String secrethelp = """
                                             Uses the secret list for the next action.
                                                                                       
                                             How to use command:
                                             secret + {command} + [parameters]
                                                                                    
                                             Alternative: s
                                                                                        
                                             E.g.  secret new "Solo Leveling"
                                                   secret add a sl SL
                                                   s read sl 5
                                                                                       
                                             {command} = other command to be executed
                                             [parameters] = parameters needed by {command}
                                                                                          
                                                                                  
                                             all [...] are optional values
                                             But if you decide to not use an optional value but the last one, you have to fill it with something like null or 0.
                                             """;

    private static final String readhelp = """
                                           Sets the value of page/chapter read to.
                                           Automatically changes lastread to now.
                                                                                      
                                           How to use command:
                                           {command} + {book} + {value}
                                                                                    
                                           Alternatives:
                                                read: r
                                                read-to: readto, rt
                                                                                      
                                           E.g.  read tbate 5
                                                 rt "Solo Leveling" 164
                                                                                      
                                           {book} = name or abbreviation of the book
                                                                                    
                                           read {value} = how many pages or chapters you have read
                                           read-to {value} = which page or chapter have you read to
                                           """;

    private static final String changehelp = """
                                             Changes the value of one of the entries by type.
                                             Attention!! If there are multiple Entries to the given book or abb it will change a random one (for now).
                                                                                          
                                             How to use command:
                                             change + {type} + {book} + {new-value} + [optional]
                                                                                    
                                             Alternative: c
                                                                                        
                                             E.g.  c link www.link.com/sl www.someotherlink.com/sl
                                                   change name tbate "Beginning after End"
                                                   
                                             Special change:
                                             If type is set to abbreviation then the abbreviation given as {book} will be replaced.
                                                    change abb SL sl
                                                    --> only sl can be used now
                                                                                        
                                             {type}         type of data to change
                                                                options:
                                                                    name = [name, n]
                                                                    link = [link, lk]
                                                                    writing-status = [ws] ∈ {Coming-Up, Rolling, Paused, Ended}
                                                                    reading-status = [rs] ∈ {Not-Started, Started, Reading, Waiting, Paused, Done}
                                                                    abbreviation = [abbreviation, ab]
                                             {book}         name or abbreviation of the book
                                             {new-value}    value to be set as replacement
                                             [optional]     When changing reading-status to paused or waiting, pauseduntil can be set manually.
                                                                format: dd.MM.yyyy
                                                                standard: 4 weeks
                                             """;

    private static final String addhelp = """
                                          Adds the abbreviation to the given entry.
                                          Attention!! If there are multiple Entries to the given book or abb it will change a random one (for now).
                                                                                       
                                          How to use command:
                                          add + {type} + {book} + {new-value}
                                                                                    
                                          Alternative: a
                                                                                     
                                          E.g.  add ab SL sl
                                                a abb "The Beginning after The End" TBATE
                                                                                     
                                          {type} = type of data to change
                                                 options:
                                                     abbreviation = [abbreviation, ab]
                                          {book} = name or abbreviation of the book
                                          {new-value} = value to be set as replacement
                                          """;

    private static final String listhelp = """
                                           Prints a list of all entries with a chosen values.
                                           Form:
                                                {name1} --> {value1.1}, {value1.2}
                                                {name2} --> {value2.1}, {value2.2}
                                                {name3} --> {value3.1}, {value3.2}
                                                ...
                                              
                                           How to use command:
                                           list + [type] + [filters] + [sortby] + [groupby]
                                                                                    
                                           Alternative: l
                                                                                      
                                           E.g.  list link read ws lr<01.01.2022
                                                 l ab r>50
                                                 
                                                                                      
                                           [type] = type of data to list
                                                    standard: name
                                                    options:
                                                        lastread = [lr]
                                                        name = [name, n]
                                                        link = [link, lk]
                                                        pauseduntil = [pu]
                                                        abbreviation = [abbreviation, ab]
                                                        read-to = [read-to, readto, rt, r]
                                                        writing-status = [ws] ∈ {Coming-Up, Rolling, Paused, Ended}
                                                        reading-status = [rs] ∈ {Not-Started, Started, Reading, Waiting, Paused, Done}
                                           [filters] = filters to exclude options not fitting (multiple possible)
                                                    style:
                                                        {type}={value}
                                                        {type}<{value}
                                                        {type}>{value}
                                                    {type} = type of data to filter by
                                                    options:
                                                        using "="
                                                            writing-status = [ws] ∈ {Coming-Up, Rolling, Paused, Ended}
                                                            reading-status = [rs] ∈ {Not-Started, Started, Reading, Waiting, Paused, Done}
                                                        using "<" or ">"
                                                            read-to = [read-to, readto, rt, r]
                                                            lastread = [lr] (Format: dd.MM.yyyy)
                                                            pauseduntil = [pu] (Format: dd.MM.yyyy)
                                           [sortby] = type of data to sort by
                                                    standard: readto
                                                    style: sortby={type} [sb, sort, sortBy]
                                                    options:
                                                        lastread = [lr]
                                                        name = [name, n]
                                                        pauseduntil = [pu]
                                                        reading-status = [rs]
                                                        writing-status = [ws]
                                                        read-to = [read-to, readto, rt, r]
                                           [groupby] = type of data to group by
                                                    style: groupby={type}  [gb, group, groupBy]
                                                    options:
                                                        reading-status = [rs]
                                                        writing-status = [ws]
                                           """;


    private static final String showhelp = """
                                           Prints the chosen entry with a the chosen value(s).
                                           Form:
                                                {value1}
                                                {value2}
                                                {value3}
                                                ...
                                              
                                           How to use command:
                                           show + {book} + [type...]
                                                                                    
                                           Alternative: sh
                                                                                      
                                           E.g.  show "Solo Leveling"
                                                 sh sl rt lk ab
                                                 
                                                              
                                           {book} = name or abbreviation of the book
                                           [type] = type of data to change
                                                    standard: shows all attributes
                                                    options:
                                                        lastread = [lr]
                                                        link = [link, lk]
                                                        pauseduntil = [pu]
                                                        reading-status = [rs]
                                                        writing-status = [ws]
                                                        read-to = [read-to, readto, rt, r]
                                                        abbreviation = [abbreviation, ab]
                                           """;
    private static final String recommendhelp = """
                                                Prints a recommendation of what to read next.
                                                Form:
                                                    Not-Started:
                                                        {books}
                                                        ...
                                                    Started:
                                                        {books}
                                                        ...
                                                    Reading (more than 2 chapters available):
                                                        {books}
                                                        ...
                                                   
                                                How to use command:
                                                recommend
                                                                                         
                                                Alternative: rec
                                                      
                                                                   
                                                {book} = name or abbreviation of the book
                                                [type] = type of data to change
                                                         standard: shows all attributes
                                                         options:
                                                             lastread = [lr]
                                                             link = [link, lk]
                                                             pauseduntil = [pu]
                                                             reading-status = [rs]
                                                             writing-status = [ws]
                                                             read-to = [read-to, readto, rt, r]
                                                             abbreviation = [abbreviation, ab]
                                                """;

    private static final String openhelp = """
                                           Opens the link of the given book.
                                              
                                           How to use command:
                                           open + {book}
                                                                                    
                                           Alternative: o
                                                                                      
                                           E.g.  open sl
                                                 o "Latna Saga"
                                                 
                                           {book} = name or abbreviation of the book
                                           """;

    private static final String listallhelp = """
                                              Prints a list of all Entries with all parameters.
                                              Form: {name}, {readto}, ..., [{abb1}, {abb2}, ...]
                                                                                         
                                              How to use command:
                                              list-all
                                                                                            
                                              Alternative: la
                                              """;

    private static final String legend = """
                                                       
                                         Legend:
                                         [...] - optional
                                         {...} - parameter
                                         "..." - abbreviation and book name in quotes if more than one word
                                         """;


    static String help(List<String> parts) {
        if (parts.size() == 1) return standardhelp;

        return switch (representation(parts.get(1))) {
            case "nw" -> newhelp;
            case "r", "rt" -> readhelp;
            case "c" -> changehelp;
            case "a" -> addhelp;
            case "l" -> listhelp;
            case "sh" -> showhelp;
            case "rec" -> recommendhelp;
            case "la" -> listallhelp;
            case "o" -> openhelp;
            case "s" -> secrethelp;
            default -> errorMessage("1");
        } + legend;
    }

    static String errorMessage(String error) {
        return switch (error) {
            case "1" -> "Invalid Input. Use help for more info.";
            case "2" -> "The given book is already in the list.";
            case "3" -> "The given book was not found. If you want to add a new Entry use \"new\".";
            case "4" -> "The read-to value of the given book is not a number. Use command read-to to adjust.";
            case "5" -> "The provided link seems to be wrong. Please correct the link with \"change link\" and try again. For " +
                    "more info on how to change link use \"help change\".\nThe provided link is:";
            case "6" -> "Your OS does not support opening a link. Copy the following link and paste it to a browser of your " +
                    "choosing:\n";
            case "7" -> "The given abbreviation is already used on another Book. Please try another one.";
            default -> error;
        };
    }

    public static String representation(String part) {
        return switch (part) {
            case "exit", "e" -> "e";
            case "new" -> "nw";
            case "read", "r" -> "r";
            case "read-to", "readto", "rt" -> "rt";
            case "reading-status", "readingstatus", "rs" -> "rs";
            case "writing-status", "writingstatus", "ws" -> "ws";
            case "add", "a" -> "a";
            case "change", "c" -> "c";
            case "list", "l" -> "l";
            case "link", "lk" -> "lk";
            case "lastread", "lr" -> "lr";
            case "pauseduntil", "pu" -> "pu";
            case "list-all", "la" -> "la";
            case "show", "sh" -> "sh";
            case "recommend", "rec" -> "rec";
            case "secret", "s" -> "s";
            case "open", "o" -> "o";
            case "help", "h" -> "h";
            case "name", "n" -> "n";
            case "sortby", "sortBy", "sort", "sb" -> "sb";
            case "groupby", "groupBy", "group", "gb" -> "gb";
            case "abbreviation", "ab" -> "ab";
            case "descending", "desc", "d" -> "desc";
            default -> part;
        };
    }
}