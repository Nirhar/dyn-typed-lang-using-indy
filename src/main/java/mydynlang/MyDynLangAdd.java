package mydynlang;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.util.function.Function;

public class MyDynLangAdd {

    // Bootstrap method
    public static CallSite bootstrap(
        MethodHandles.Lookup lookup,
        String name,
        MethodType type
    ) throws Exception {
        // The Mutable Callsite here transitions as below:
        // Dispatcher based on Types -> Integer Add OR String Concat
        MutableCallSite callsite = new MutableCallSite(type);

        MethodHandle dispatcher = lookup.findStatic(
            MyDynLangAdd.class,
            "dispatcher",
            MethodType.methodType(
                /*Return type=*/ String.class,
                MutableCallSite.class,
                MethodHandles.Lookup.class,
                String.class,
                String.class
            )
        );
        dispatcher = dispatcher.bindTo(callsite); // Binds callsite as the first argument
        dispatcher = dispatcher.bindTo(lookup); // Binds lookup as the second argument

        callsite.setTarget(dispatcher);
        return callsite;
    }

    public static String dispatcher(
        MutableCallSite callsite,
        MethodHandles.Lookup lookup,
        String left,
        String right
    ) throws Throwable {
        Function<String, Boolean> tryParseInt = s -> {
            try {
                int asNum = Integer.parseInt(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        };

        Boolean leftIsInt = tryParseInt.apply(left);
        Boolean rightIsInt = tryParseInt.apply(right);

        // Perform type-checking
        MethodHandle newmh;
        if (leftIsInt && rightIsInt) {
            newmh = lookup.findStatic(
                MyDynLangAdd.class,
                "addInts",
                MethodType.methodType(String.class, String.class, String.class)
            );
        } else if (leftIsInt && !rightIsInt) {
            throw new RuntimeException(
                "Cannot add String to Integer, LHS has to be String!"
            );
        } else {
            newmh = lookup.findStatic(
                MyDynLangAdd.class,
                "concatStrings",
                MethodType.methodType(String.class, String.class, String.class)
            );
        }

        // Insert automatic conversion from Object to String and vice versa
        newmh = newmh.asType(callsite.type());

        // We can use ConstantCallSite for our case, because our indy would be executed only once.
        callsite.setTarget(newmh);

        return (String) newmh.invoke(left, right);
    }

    public static String addInts(String a, String b) {
        int aint = Integer.parseInt(a);
        int bint = Integer.parseInt(b);
        return Integer.toString(aint + bint);
    }

    public static String concatStrings(String a, String b) {
        return a + " " + b;
    }
}
