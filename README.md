# Kokopelli 5

Standalone versie van de spelletjes.

## Development

Om te ontwikkelen moet je een aantal dingen eerst installeren:

- NodeJS 18
- Java 21
  - [Quarkus cli](https://quarkus.io/get-started/)
- Docker
- Make

Daarna kan je vanuit de root de volgende commands doen:

Om de backend op te starten (inclusief database etc.)

```bash
make backend
```

Voor de frontend (inclusief dependencies etc.)

```bash
make frontend
```

Vervolgens zal de backend op http://localhost:8080 draaien en de frontend op http://localhost:5173

## Database

In development zorgt Quarkus voor de database, deze start automagisch op in docker.
Je kan hem vanuit HeidiSQL (o.i.d.) bereiken met

- username: root
- password: quarkus
- port: 44444
- ip: 127.0.0.1

## Spelletjes

Een spelletje is gedefineerd in `/src/main/resources/games` als los json bestand. Deze wordt ingeladen zodra de applicatie start en zal worden uitgekozen door de game manager.

Er zijn vele opties beschikbaar die je kan gebruiken om een spelletje te defineren, een aantal zijn verplicht, die staan hier beschreven. De rest zal je uit de huidge spelletjes moeten halen

| json key       | voorbeeld waarde                                                                              | beschrijving                                                                          | verplicht |
| -------------- | --------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------- | --------- |
| id             | nl-alert                                                                                      | URL safe id van het spelletje                                                         | Ja        |
| title          | NL Alert                                                                                      | Leesvriendelijke versie van de ID                                                     | Ja        |
| description    | Laatste die de ruimte verlaat verliest!                                                       | Beschrijving van het spelletje                                                        | Ja        |
| players.min    | 0                                                                                             | Minimum aantal spelers verreist                                                       | Ja        |
| players.max    | 4                                                                                             | Maximum aantal speler                                                                 | Nee       |
| game.title     | NL Alert!                                                                                     | Eerste regel op het spelletjes scherm, is het grootst                                 | Ja        |
| game.first_row | De eerste die de `<span class='alt'>ruimte/tuin</span>` verlaat vouwt een `<span>bak</span>`! | Zin die het spelletje uitlegd, hier kan HTML gebruikt worden, zie hieronder voor meer | Ja        |

### Speler selectie

Heeft je spel spelers? Die kan je selecteren met `@0 t/m @9`

Voorbeelden:

```js
game.first_row = '@0 jij gaat de <span>bus</span> in!'
```

```js
game.title = '@0'
game.first_row = '@1 en @2 gaan een cocktail voor jou maken!'
```

### HTML

In het game object kan HTML gebruikt worden, de twee tags die nu bruikbaar zijn zijn:

`<span>` Deze maakt de tekst blauw, gebruik voor straffen

`<span class='alt'>` Deze maakt de tekst geel, gebruik om iets uit het spel te highlighten

### Voorbeeld JSON

```json
{
	"id": "sokken-check",
	"title": "Sokken Check",
	"description": "Checkt of je sokken aan hebt",
	"players": {
		"min": 0
	},
	"game": {
		"title": "Sokken check!",
		"first_row": "Iedereen die op dit moment <span class='alt'>geen</span> sokken aan heeft, vouwt een <span>bak</span>!"
	}
}
```
