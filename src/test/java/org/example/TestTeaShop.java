package org.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.List;

public class TestTeaShop {
    private WebDriver driver;

    @Before
    public void pc() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
    //     ТЕСТ 1. Поиск по сайту
    @Test
    public void checkSearch() throws InterruptedException {
        //1) Открыть главную страницу сайта
        driver.get("https://www.chay.info");
        //2) Нажать на кнопку «поиск».
        WebElement searchButton = driver.findElement(By.xpath("//*[@id=\"header__bar\"]/div[2]/div/div/a[2]/div/div[2]"));
        searchButton.click();
        //3) Ввести наименование товара в поле поиска
        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("Габба");
        // 4) Нажать на кнопку с изображением лупы
        WebElement searchButton_1 = driver.findElement(By.xpath("//*[@id=\"header__bar\"]/div[1]/div/ul/li[1]/div/div[1]/form/div/button[2]"));
        searchButton_1.click();
        //Ожидание: Открыта страница с результатами поиска с соответствующими наименованию данными
        //Проверка: URL открывшейся страницы содержит в себе закодированное значение введенного наименования
        // 1) Получение текущего URL
        String currentUrl = driver.getCurrentUrl();
        // Проверка, содержит ли URL поисковый запрос
        Assert.assertTrue(currentUrl.contains("%D0%93%D0%B0%D0%B1%D0%B1%D0%B0"));
    }
    //    ТЕСТ 2. Добавление товара в корзину
    @Test()
    public void checkAddInCart() {
        //          1. Открыть главную страницу сайта
        driver.get("https://www.chay.info");
        //          2. Перейти в раздел «Чай»
        WebElement teaSection = driver.findElement(By.xpath("//*[@id=\"header__bar\"]/div[2]/div/ul/li[1]/a/div/div[1]"));
        teaSection.click();
        //          3. Добавить в корзину первый товар из открывшегося раздела
        WebElement addProduct = driver.findElement(By.xpath("(//div[@class='icon-plus__icon indent-r--s'])[1]"));
        addProduct.click();

        // Получить ID первого товара из открывшегося раздела
        WebElement product = driver.findElement(By.xpath("(//div[@class='catalog-item__content'])[2]/div[7]/div[1]/button"));
        String expectedProductId = product.getAttribute("data-id");

        //          4. Перейти в корзину
        WebElement openCart = driver.findElement(By.id("header_basket_icon"));
        openCart.click();

        //Проверка: 1) Количество товара в корзине увеличилось с 0 до 1
        WebElement cnt = driver.findElement(By.xpath("//*[@id=\"header_basket_cnt\"]"));
        String count = cnt.getText();
        Assert.assertEquals(count,"1");

        // Проверка: 2) ID выбранного товара соответствует ID товара в корзине
        WebElement productInCart = driver.findElement(By.xpath("//ul[@class='cart__list basket-items-list-table']/li[1]"));
        String actualProductId = productInCart.getAttribute("product-id");
        Assert.assertEquals(expectedProductId, actualProductId);
    }

    //     ТЕСТ 3. Применение фильтров на странице
    @Test()
    public void checkfilters() throws InterruptedException {
        //        1. Открыть главную страницу сайта
        driver.get("https://www.chay.info");
        //        2. Перейти в раздел «Чай»
        WebElement teaSection = driver.findElement(By.xpath("//*[@id=\"header__bar\"]/div[2]/div/ul/li[1]/a/div/div[1]"));
        teaSection.click();
        // Прокрутка страницы вниз на 500 пикселей для получения видимости компонента «Фильтры»
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,500)");
        //        3. Заполнить фильтры
        //        3.1 Вид
        WebElement typeOfTeaFilter = driver.findElement(By.xpath("(//div[@class='filters__inner']//div)[1]/div[1]/span"));
        typeOfTeaFilter.click();

        WebElement selectSecondFilter = driver.findElement(By.xpath("//*[@id=\"filter-form\"]/div/div/div/div[1]/div[2]/div/div/div[2]/label/div"));
        selectSecondFilter.click();
        //        3.2 Цена
        WebElement priceFilter = driver.findElement(By.xpath("//*[@id=\"filter-form\"]/div/div/div/div[2]"));
        priceFilter.click();

        WebElement selectPriceFilter = driver.findElement(By.xpath("//*[@id=\"filter-form\"]/div/div/div/div[2]/div[2]/div/div/div[2]/label/div"));
        selectPriceFilter.click();

        // Задержка для загрузки применения фильтров
        Thread.sleep(3000);

        // Получение количества ожидаемых отфильтрованных записей
        WebElement qtOfRows= driver.findElement(By.xpath("(//div[contains(@class,'catalog-presents__checkbox-count check_count_DOP_PRICE_SPB')])[2]"));
        String expectedProductCount = qtOfRows.getText();
        //        4. Нажать на кнопку «Показать»
        WebElement showBtn= driver.findElement(By.id("set_filter_btn"));
        showBtn.click();

        // Расчет количества выведенных карточек товара на страницу
        List<WebElement> productCards = driver.findElements(By.xpath("//div[contains(@class,'catalog-page__cards')]"));
        int actualProductCount = productCards.size();
        actualProductCount = actualProductCount *3;

        // Проверка: Количество карточек товара на странице соответствует количеству, указанному в фильтре
        Assert.assertEquals(actualProductCount, Integer.parseInt(expectedProductCount));
    }

    //     ТЕСТ 4. Просмотр статьи на сайте
    @Test()
    public void checkArticles() throws InterruptedException {
        //        1. Открыть главную страницу сайта.
        driver.get("https://www.chay.info");
        //        2. Перейти в раздел «Читать»
        WebElement readSection = driver.findElement(By.xpath("//*[@id=\"header__bar\"]/div[1]/div/div[3]/ul/li[2]/a"));
        readSection.click();
        //        3. Перейти на вкладку, например, «Чай и здоровье»
        WebElement goToSection = driver.findElement(By.xpath("(//div[@class='tabs']//div)[1]/div[1]/a[2]"));
        goToSection.click();
        Thread.sleep(5000);
        //        4. Выбрать вторую статью
        WebElement goToArticle = driver.findElement(By.xpath("(//div[@class='article-card__title']//a)[3]"));

        // Получить href выбранной статьи
        WebElement article = driver.findElement(By.xpath("(//div[contains(@class,'articles-grid__item articles-grid-sizer')])[2]/div[1]/div[1]/a"));
        String expectedArticle = article.getAttribute("href");

        goToArticle.click();

        // Получить href открывшейся статьи
        WebElement openedArticle= driver.findElement(By.xpath("/html/head/link[1]"));
        String actualArticle = openedArticle.getAttribute("href");

        //Проверка: href открывшейся статьи соответствует href статьи, которую выбрали в п.4
        Assert.assertEquals(expectedArticle, actualArticle);
    }
    //     ТЕСТ 5. Добавление товара в избранное
    @Test()
    public void checkAddToFavorites() {
        //          1. Открыть главную страницу сайта
        driver.get("https://www.chay.info");
        //          2. Перейти в раздел «Чай»
        WebElement teaSection = driver.findElement(By.xpath("//*[@id=\"header__bar\"]/div[2]/div/ul/li[1]/a/div/div[1]"));
        teaSection.click();
        //          3. Добавить в избранное второй товар из открывшегося раздела
        WebElement addProduct = driver.findElement(By.xpath("(//button[contains(@class,'button button--view--icon-secondary')])[2]"));
        addProduct.click();
        // Получить ID второго товара из открывшегося раздела
        WebElement product = driver.findElement(By.xpath("(//div[@class='catalog-item__content'])[2]/div[7]/div[1]/button"));
        String expectedProductId = product.getAttribute("data-id");
        //          4. Перейти в избранное
        WebElement openCart = driver.findElement(By.xpath("//li[@class='header__top-controls-item header__top-controls-item--favorite']//a[1]"));
        openCart.click();

        //Проверка: 1) Количество товара в избранном увеличилось с 0 до 1
        WebElement cnt = driver.findElement(By.xpath("(//div[@class='header__top-count'])[1]"));
        String count = cnt.getText();
        Assert.assertEquals(count,"1");

        // 2) Проверка, что ID добавленного товара соответствует ID товара в избранном
        // Получить ID товара в избранном
        WebElement productInFavourite = driver.findElement(By.xpath("(//div[@class='favorites__inner']//div)[1]/div[1]/div[1]/div[6]/div[1]/button"));
        String actualProductId = productInFavourite.getAttribute("data-id");
        Assert.assertEquals(expectedProductId, actualProductId);
    }

    @After
    public void close() {
        driver.close();
    }

}
