# reflect-study

`java.lang.reflect.Method`

Method[] methods = userController.getClass().getDeclaredMethods();

getDeclaredMethods()와 getMethods()의 차이점

#### getDeclaredMethods()
그 파일에 메서드만 가져온다.

#### getMethods()
상속받은 메서드 모두 가져온다. `Object`메서드도 가져온다.


