# QA Bot

## Razvoj

### Vzpostavitev razvojnega okolja

- Maven
- Java (>=17)
- (opcijsko) Docker

### Zagon razvojne verzije

V kolikor imate na sistemu nameščeno orodje Maven ter ustrezno verzijo Jave (>=17) je zagon aplikacije enostaven preko ukazne vrstice:

```mvn springboot:run```

#### Starejša različica Jave

V kolikor uporabljate starejšo različico Jave (npr. 11 ali 1.8) je za delovanje potrebnih nekaj sprememb oz. prilagoditev.

- V datoteki pom.xml spremenite sledeče:
   - Verzijo na *spring-boot-starter-parent* na 2.7.15
   - Lastnost *<java.version>* na verzijo 11 oz. 1.8
- V datoteki *services/FaqService.java* zakomentirajte ali izbrišite *@PostConstruct* metodo ter odstranite vključitveno vrstico na vrhu datoteke.

#### Alternativni zagon z uporabo Docker

TODO
