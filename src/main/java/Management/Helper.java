package Management;

class Helper {

    private static final String standardhelp = """
                                               Use one of the following commands:
                                               new "{book-name}" [read-to] [link] [acronyms...]
                                               read "{anym/book}" {pages-read}
                                               read-to "{anym/book}" {page-read-to}
                                               list [type]
                                               list-all
                                               add acronym "{anym/book}" "{new-value}"
                                               change {type} "{anym/book}" "{new-value}"
                                               help (or h)
                                                                                              
                                               secret {command} [parameters]
                                                       
                                               Legend:
                                               [...] - optional
                                               {...} - parameter
                                               "..." - acronym and book name in quotes if more than one word
                                                       
                                               For details use help (or h) {command}
                                               """;

    private static final String newhelp = """
                                          Adds a new book with given parameters to the list.
                                                                                    
                                          How to use new:
                                          new + {book-name} + [page] + [link] + [acronyms...]
                                                                                     
                                          E.g.  new "Solo Leveling"
                                                new "Tales of Demons and Gods" 358 www.somelink.com/todag
                                                new "The Beginning after The End" 164 null TBATE tbate "Arthur Leywin"
                                                                                    
                                          [page] = page or chapter you have read to
                                          [link] = online link to e-books
                                          [acronyms...] = multiple acronyms or abbreviations of book-name
                                                          listed as multiple whitespace seperated words       
                                                                                     
                                          all [...] are optional values
                                          But if you decide to not use an optional value but the last one, you have to fill it with something like null or 0.                                   
                                          """;

    private static final String secrethelp = """
                                             Uses the secret list for the next action.
                                                                                       
                                             How to use new:
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
                                                                                      
                                           How to use read/read-to:
                                           {command} + {anym/book} + {value}
                                                                                    
                                           Alternatives: 
                                                read: r
                                                read-to: readto, rt
                                                                                      
                                           E.g.  read tbate 5
                                                 rt "Solo Leveling" 164
                                                                                      
                                           {anym/book} = name or acronym of the book  
                                                                                    
                                           read {value} = how many pages or chapters you have read
                                           read-to {value} = which page or chapter have you read to                                      
                                           """;

    private static final String changehelp = """
                                             Changes the value of one of the entries by type.
                                             Attention!! If there are multiple Entries to the given book or anym it will change a random one (for now).
                                                                                          
                                             How to use change:
                                             change + {type} + {anym/book} + {new-value}
                                                                                    
                                             Alternative: c
                                                                                        
                                             E.g.  c link SL www.someotherlink.com/sl 
                                                   change name tbate "Beginning after End"
                                                   
                                             Special change:
                                             If type is set to acronym then the acronym given as {anym/book} will be replaced.
                                                    change anym SL sl
                                                    --> only sl can be used now
                                                                                        
                                             {type} = type of data to change
                                                    options:
                                                        link = [links, link, lk]
                                                        name = [names, name, n]
                                                        acronym = [acronyms, acronym, anyms, anym, a]
                                             {anym/book} = name or acronym of the book
                                             {new-value} = value to be set as replacement
                                             """;

    private static final String addhelp = """
                                          Adds the acronym to the given entry.
                                          Attention!! If there are multiple Entries to the given book or anym it will change a random one (for now).
                                                                                       
                                          How to use change:
                                          add + {type} + {anym/book} + {new-value}
                                                                                    
                                          Alternative: a
                                                                                     
                                          E.g.  add a SL sl 
                                                a anym "The Beginning after The End" TBATE
                                                                                     
                                          {type} = type of data to change
                                                 options:
                                                     acronym = [acronyms, acronym, anyms, anym, a]
                                          {anym/book} = name or acronym of the book
                                          {new-value} = value to be set as replacement
                                          """;

    private static final String listhelp = """
                                           Prints a list of all Entries with a chosen values.
                                           Form:
                                                {name1} --> {value1}
                                                {name2} --> {value2}
                                                {name3} --> {value3}
                                                ...
                                              
                                           How to use list:
                                           list + [type]
                                                                                    
                                           Alternative: l
                                                                                      
                                           E.g.  list link
                                                 l acronym
                                                 
                                                                                      
                                           [type] = type of data to change
                                                    standard: name
                                                    options:
                                                        link = [links, link, lk]
                                                        name = [names, name, n]
                                                        read-to = [read-to, read, r]
                                                        acronym = [acronyms, acronym, anyms, anym, a]
                                           {anym/book} = name or acronym of the book
                                           {new-value} = value to be set as replacement
                                           """;

    private static final String listallhelp = """
                                              Prints a list of all Entries with all parameters.
                                              Form: name={name}, readto={readto}, ..., acronyms=[{anym1}, {anym2}, ...]
                                                                                         
                                              How to use list-all:
                                              list-all
                                                                                            
                                              Alternative: la
                                              """;

    private static final String notice = """
                                                                                  
                                         All values which are longer than one word have to be "surronded in quotes".
                                         """;


    static String help(String[] parts) {
        if (parts.length == 1) return standardhelp;

        return switch (representation(parts[1])) {
            case "nw" -> newhelp;
            case "r", "rt" -> readhelp;
            case "c" -> changehelp;
            case "a" -> addhelp;
            case "l" -> listhelp;
            case "la" -> listallhelp;
            case "s" -> secrethelp;
            default -> errorMessage("invalid");
        } + notice;
    }

    static String errorMessage(String error) {
        return switch (error) {
            case "invalid" -> "Invalid Input. Use help for more info.";
            case "book already there" -> "The given book is already in the list.";
            case "enf" -> "The given book was not found. If you want to add a new Entry use \"new\".";
            case "read-to not number" -> "The read-to value of the given book is not a number. Use command read-to to adjust.";
            default -> "";
        };
    }

    static String representation(String part) {
        return switch (part) {
            case "exit", "e" -> "e";
            case "new" -> "nw";
            case "read", "r" -> "r";
            case "read-to", "readto", "rt" -> "rt";
            case "add", "a" -> "a";
            case "change", "c" -> "c";
            case "list", "l" -> "l";
            case "links", "link", "lk" -> "lk";
            case "list-all" -> "la";
            case "secret", "s" -> "s";
            case "help", "h" -> "h";
            case "names", "name", "n" -> "n";
            case "acronyms", "acronym", "anyms", "anym", "ac" -> "ac";
            default -> " ";
        };
    }
}
