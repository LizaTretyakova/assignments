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
        used.put(resName, true);

        for (Class<?> paramType : paramTypes) {
            String parameterTypeName = paramType.getName();//next parameter type we need
//            if(used.containsKey(parameterTypeName) && used.get(parameterTypeName)) {
//                throw new InjectionCycleException();
//            }

            int cnt = 0;
            String name = new String();
            for (String implName : implClassNames) {//search for implementation of the current parameter type
                if (Class.forName(parameterTypeName).isAssignableFrom(Class.forName(implName))) {
                    if (cnt > 0) {
                        throw new AmbiguousImplementationException();
                    }
                    cnt++;
                    name = implName;
                }
            }
            if (cnt == 0) {
                throw new ImplementationNotFoundException();
            }
            if(!instances.containsKey(name)) {
                if(used.containsKey(name) && used.get(name)) {
                    throw new InjectionCycleException();
                }
                instances.put(name, createObjectByName(name));
            }
            params.add(instances.get(name));
        }

        used.put(resName, false);
        return Class.forName(resName).getConstructors()[0].newInstance(params.toArray());
    }
}