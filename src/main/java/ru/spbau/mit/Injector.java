package ru.spbau.mit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Injector {
    private static HashMap<String, Boolean> used = new HashMap<>();
    private static HashMap<String, Object> instances = new HashMap<>();
    private static List<String> implClassNames;

    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        implClassNames = new ArrayList<>(implementationClassNames);
        implClassNames.add(rootClassName);
        Object res;
        try {
            res = createObjectByName(rootClassName);
        } catch (InjectionCycleException | ImplementationNotFoundException | AmbiguousImplementationException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            throw new ImplementationNotFoundException();
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return res;
    }

    private static Object createObjectByName(String resName) throws Exception {
        //getting parameters' types we need to create an instance of resName
        List<Class<?>> paramTypes = Arrays.asList(Class.forName(resName).getConstructors()[0].getParameterTypes());
        ArrayList<Object> params = new ArrayList<>();

        for (Class<?> paramType : paramTypes) {
            String parameterTypeName = paramType.getName();//next parameter type we need
            if(used.containsKey(parameterTypeName) && used.get(parameterTypeName)) {
                throw new InjectionCycleException();
            }
            used.put(parameterTypeName, true);

            int cnt = 0;
            for (String implName : implClassNames) {//search for implementation of the current parameter type
                if (Class.forName(parameterTypeName).isAssignableFrom(Class.forName(implName))) {
                    if (cnt > 0) {
                        throw new AmbiguousImplementationException();
                    }
                    cnt++;
                    if (!instances.containsKey(parameterTypeName)) {
                        instances.put(parameterTypeName, createObjectByName(implName));
                    }
                }
            }
            if (cnt == 0) {
                throw new ImplementationNotFoundException();
            }
            params.add(instances.get(parameterTypeName));
            used.put(parameterTypeName, false);
        }


        return Class.forName(resName).getConstructors()[0].newInstance(params.toArray());
    }
}