# QA Bot

## Razvoj

### Vzpostavitev razvojnega okolja

- Maven
- Java (>=17)
- (opcijsko) Docker

### Zagon razvojne verzije

V kolikor imate na sistemu nameščeno orodje Maven ter ustrezno verzijo Jave (>=17) je zagon aplikacije enostaven preko ukazne vrstice:

```mvn spring-boot:run```

#### Starejša različica Jave

V kolikor uporabljate starejšo različico Jave (npr. 11 ali 1.8) je za delovanje potrebnih nekaj sprememb oz. prilagoditev.

- V datoteki pom.xml spremenite sledeče:
   - Verzijo na *spring-boot-starter-parent* na 2.7.15
   - Lastnost *<java.version>* na verzijo 11 oz. 1.8
- V datoteki *services/FaqService.java* zakomentirajte ali izbrišite *@PostConstruct* metodo ter odstranite vključitveno vrstico na vrhu datoteke.

#### Alternativni zagon z uporabo VSCode in Docker

V orodju VS Code v kolikor še nimate, namestite vtični *Dev containers*. Po uspešni namestitvi vtičnika odprite ukazno paleto (angl. Command Palette) ter vpišite v iskalno polje *Dev Containers*.

Iz spustnega seznama izberite možnost *Dev Containers: Open Folder in Container...*, v izbirniku datotek poiščite projekt in ga odprite.

Ob prvem zagonu traja nekaj minut, da VS Code prenese Docker sliko in znotraj slike namesti vse potrebno. Po koncu se vam odpre okno z izbranim projektom, ki teče znotraj Docker okolja.

## Veliki jezikovni modeli

[Povezava do uporabljenih velikih jezikovnih modelov (LLM).](https://huggingface.co/GregaVrbancic/OTS_2023)
