#Некоторый разбор в начале
В программе кусок кода есть
listOfShipsNCoordinates.computeIfAbsent(Ship.BATTLESHIP1, k -> new ArrayList<>())
        .add(new Coordinate(0,1));
Когда добавляем с помощью computeIfAbsent().add() значение в коллекцию, то при отсутствии ключа - создается новая коллекция к соответствующему ключу, если ключ есть, то значение добавляется в существующую коллекцию.

#РАБОТА С КОПИРОВАНИЕМ

Когда копируем объекты, то есть два типа копирования - простое и глубокое. При простом копировании создается копия объекта, но если там есть изменяющиеся значения (Mutable) (к ним относятся например классы с изменяемыми полями, коллекции), то при изменении скопированного объекта изменится и оригинальный, поэтому существует глубокое копирование. Оно тоже бывает с разным синтаксисом. В моем коде при копировании EnumMap несколько сложностей возникло, поэтому распишу тут как это всё работает.

Глубокое копирование это нескольких сложных механизмов: new, копирования коллекций и иногда  clone().

Если писать NameClass copy = original, то копирования вообще не будет, так как обе переменные ссылаются на один объект.

Если сделать NameClass copy - new NameClass(original); и внутри констурктора правильно скопировать поля, то получим другой объект, это уже копирование самого NewClass. Но внутри может быть NameClass original -> mutableValue, тогда copy ->mutableValue, но глубокая копия должна дать newValue.

Обычный класс: copy constructor - самый контролиремый способ:
```class NewClass {

private string name;
private Value value;

public NewClass(string name, Value value){
this.name;
this.value;
}

public NewClass(NewClass other) {
this.name;//скопировать примитив или immutable
this.value = new Value(other.value);//скопировать с созданием нового объекта mutable объект
	}
}```

Здесь конструкция public NewClass(NewClass other) называется копировальным конструктором. Потом можно писать NewClass copy = new Newclacc(riginal); который можно расшифровывать как "создать новый NewClass, используя существующий NewClass original как источник данных.

List<> не является неизменяемым, а так же имеет вложенные в себя объекты, оторые так же могут быть mutable, поэтому:
this.value = new ArrayList<>(other.value);

У record автоматически создается canonical constructor, поэтому для создания копии можно просто писать NewClass copy = new Newclass(riginal).

Если List<NewClass>, то нужно глубокое копирование и для record, потому что стандартного копирования его не достаточно. Стандартное копирование списка сложных объектов такое:

record NewClass (Srting name, List<OtherClass> other) {
public NewClass(NewClass otherObject) {
this(
otherObject.name(),
otherObject.other()
.stream()
.map(OtherClass::new)
.toList()
);
	}
}