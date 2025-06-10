package org.haldokan.edge.interviewquest.google;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question - probability is the overall probability of the chars in the text. I wrote
 * another solution which accounts for the probability of chars occurring after each other here: ProbabilityStringGeneration2.java
 *
 * The Question: 4_STAR
 *
 * This is a two part question related to Markov string generation.
 * <p>
 * Part 1: Read a training set of strings. For each character in any of the strings, calculate the probability of
 * seeing that character and store it for later use. (this part is about coming up with the right data structure and
 * correct probability calculation).
 * <p>
 * Part 2: Based on the training set from Part 1, generate a random string of length N.
 * <p>
 * Created by haytham.aldokanji on 5/22/16.
 */
public class ProbabilityStringGeneration1 {
    private final List<CharProbabilityRange> charProbabilityRanges = new ArrayList<>();

    public static void main(String[] args) {
        ProbabilityStringGeneration1 driver = new ProbabilityStringGeneration1();
        driver.testCalculateProbability();
        driver.testTrainAlgorithm();
        driver.testGenerateStringWithProbability1();
        driver.testGenerateStringWithProbability2();
    }

    public String generateStringWithProbability(int stringLen) {
        if (charProbabilityRanges.isEmpty()) {
            throw new IllegalStateException("Must train the algorithm first");
        }
        StringBuilder generatedString = new StringBuilder(stringLen);

        IntStream.range(0, stringLen).forEach(index -> {
            double probability = Math.random();
            // can use a modified binary search here to improve search to O(logn) - ranges already sorted
            for (CharProbabilityRange range : charProbabilityRanges) {
                if (range.inRange(probability)) {
                    generatedString.append(range.character);
                    break;
                }
            }
        });
        return generatedString.toString();
    }

    public void trainAlgorithm(List<String> strings) {
        double[] charProbabilities = calculateProbability(strings);

        double probabilityRange = 0d;
        for (int charAscii = 0; charAscii < charProbabilities.length; charAscii++) {
            double probability = charProbabilities[charAscii];

            if (probability != 0d) {
                charProbabilityRanges.add(new CharProbabilityRange((char) charAscii,
                        probabilityRange,
                        probabilityRange + probability));

                probabilityRange += probability;
            }
        }
    }

    private double[] calculateProbability(List<String> strings) {
        double[] charProbabilities = new double[256];

        for (String string : strings) {
            char[] chars = string.toCharArray();
            for (char chr : chars) {
                // avoid unicode chars that can go out of ascii range
                if (chr < 256) {
                    charProbabilities[chr] += 1d;
                }
            }
        }
        double sampleSize = strings.stream().collect(Collectors.summingDouble(String::length));
        IntStream.range(0, charProbabilities.length).forEach(index ->
                charProbabilities[index] = charProbabilities[index] / sampleSize);

        return charProbabilities;
    }

    private void testCalculateProbability() {
        String string = "thou shalt not lie";
        double[] charProbabilities = calculateProbability(Lists.newArrayList(string));

        double probabilitySum = Arrays.stream(charProbabilities).sum();
        assertThat(probabilitySum, closeTo(1, 0.0000000000000000001));

        assertThat(charProbabilities[' '], closeTo(0.166, 0.006));
        assertThat(charProbabilities['t'], closeTo(0.166, 0.006));
        assertThat(charProbabilities['h'], closeTo(0.111, 0.001));
        assertThat(charProbabilities['o'], closeTo(0.111, 0.001));
        assertThat(charProbabilities['u'], closeTo(0.0555, 0.0005));
        assertThat(charProbabilities['s'], closeTo(0.0555, 0.0005));
        assertThat(charProbabilities['a'], closeTo(0.0555, 0.0005));
        assertThat(charProbabilities['l'], closeTo(0.111, 0.001));
        assertThat(charProbabilities['n'], closeTo(0.0555, 0.0005));
        assertThat(charProbabilities['i'], closeTo(0.0555, 0.0005));
        assertThat(charProbabilities['i'], closeTo(0.0555, 0.0005));
    }

    private void testTrainAlgorithm() {
        charProbabilityRanges.clear();
        String string = "thou shalt not lie";
        trainAlgorithm(Lists.newArrayList(string));

        double firstRangeProbability = charProbabilityRanges.get(0).end;
        assertThat(firstRangeProbability, closeTo(0.166, 0.006));

        double lastRangeProbability = charProbabilityRanges.get(charProbabilityRanges.size() - 1).end;
        assertThat(lastRangeProbability, closeTo(1d, 0.0000000000001d));
    }

    private void testGenerateStringWithProbability1() {
        charProbabilityRanges.clear();
        String string = "ttttttttttttttttttttttttttttttttttttttttsssssssssssssssssssszzzzzzzzzzxxxxx" +
                "abcdefghijklmnopqrstuvwxyz";
        trainAlgorithm(Lists.newArrayList(string));

        // can't test random so let's print the strings generated with probabilities based on the trained algorithm
        for (int i = 1; i <= 30; i++) {
            System.out.println(generateStringWithProbability(20));
        }
    }

    private void testGenerateStringWithProbability2() {
        charProbabilityRanges.clear();
        String string1 = "Q: What causes global warming?" +
                "A: Global warming occurs when carbon dioxide (CO2) and other air pollutants collect in the atmosphere and absorb sunlight and solar radiation that have bounced off the earth’s surface. Normally, this radiation would escape into space—but these pollutants, which can last for years to centuries in the atmosphere, trap the heat and cause the planet to get hotter." +
                "In the United States, the burning of fossil fuels to make electricity is the largest source of heat-trapping pollution, producing about two billion tons of CO2 every year. Coal-burning power plants are by far the biggest polluters. The country’s second-largest source of carbon pollution is the transportation sector, which generates about 1.7 billion tons of CO2 emissions a year." +
                "Curbing dangerous climate change requires very deep cuts in emissions, as well as the use of alternatives to fossil fuels worldwide. The good news is that we’ve started a turnaround: CO2 emissions in the United States actually decreased from 2005 to 2014, thanks in part to new, energy-efficient technology and the use of cleaner fuels. And scientists continue to develop new ways to modernize power plants, generate cleaner electricity, and burn less gasoline while we drive. The challenge is to be sure these solutions are put to use and widely adopted." +
                "Q: Is the earth really getting hotter?" +
                "A: Yes. Over the past 50 years, the average global temperature has increased at the fastest rate in recorded history. And experts see the trend is accelerating: All but one of the 16 hottest years in NASA’s 134-year record have occurred since 2000." +
                "Climate change deniers have argued that there has been a “pause” or a “slowdown” in rising global temperatures, but several recent studies, including a 2015 paper published in the journal Science, have disproved this claim. And scientists say that unless we curb global-warming emissions, average U.S. temperatures could increase by up to 10 degrees Fahrenheit over the next century." +
                "Q: Is global warming causing extreme weather?" +
                "A: Scientists agree that the earth’s rising temperatures are fueling longer and hotter heat waves, more frequent droughts, heavier rainfall, and more powerful hurricanes. In 2015, for example, scientists said that an ongoing drought in California—the state’s worst water shortage in 1,200 years—had been intensified by 15 percent to 20 percent by global warming. They also said the odds of similar droughts happening in the future had roughly doubled over the past century. And in 2016, the National Academies of Science, Engineering, and Medicine announced that it’s now possible to confidently attribute certain weather events, like some heat waves, directly to climate change." +
                "The earth’s ocean temperatures are getting warmer, too—which means that tropical storms can pick up more energy. So global warming could turn, say, a category 3 storm into a more dangerous category 4 storm. In fact, scientists have found that the frequency of North Atlantic hurricanes has increased since the early 1980s, as well as the number of storms that reach categories 4 and 5. In 2005, Hurricane Katrina—the costliest hurricane in U.S. history—struck New Orleans; the second-costliest, Hurricane Sandy, hit the East Coast in 2012." +
                "The impacts of global warming are being felt across the globe. Extreme heat waves have caused tens of thousands of deaths around the world in recent years. And in an alarming sign of events to come, Antarctica has been losing about 134 billion metric tons of ice per year since 2002. This rate could speed up if we keep burning fossil fuels at our current pace, some experts say, causing sea levels to rise several meters over the next 50 to 150 years." +
                "Q: Why should I care?" +
                "A: Each year, scientists learn more about how global warming is affecting the planet, and many agree that environmental, economic, and health consequences are likely to occur if current trends continue. Here’s just a smattering of what we can look forward to:" +
                "Melting glaciers, early snowmelt, and severe droughts will cause more dramatic water shortages and increase the risk of wildfires in the American West." +
                "Rising sea levels will lead to coastal flooding on the Eastern Seaboard, especially in Florida, and in other areas such as the Gulf of Mexico." +
                "Forests, farms, and cities will face troublesome new pests, heat waves, heavy downpours, and increased flooding. All those factors will damage or destroy agriculture and fisheries." +
                "Disruption of habitats such as coral reefs and Alpine meadows could drive many plant and animal species to extinction." +
                "Allergies, asthma, and infectious disease outbreaks will become more common due to increased growth of pollen-producing ragweed, higher levels of air pollution, and the spread of conditions favorable to pathogens and mosquitoes." +
                "Q: Where does the United States stand in terms of global-warming contributors?" +
                "A: In recent years, China has taken the lead in global-warming pollution, producing about 28 percent of all CO2 emissions. The United States comes in second. Despite making up just 4 percent of the world’s population, we produce a whopping 16 percent of all global CO2 emissions—as much as the European Union and India (third and fourth place) combined. And America is still number one, by far, in cumulative emissions over the past 150 years. Our responsibility matters to other countries, and it should matter to us, too." +
                "Q: Is the United States doing anything to curb global warming?" +
                "A: We’ve started. But in order to avoid the worst effects of climate change, we need to do a lot more—together with other countries—to reduce our dependence on fossil fuels and start using clean energy instead." +
                "In 2015, the U.S. Environmental Protection Agency pledged to reduce carbon pollution from our power plants by nearly a third by 2030, relative to 2005 levels, through its Clean Power Plan. The U.S. Department of Transportation has proposed carbon pollution and fuel economy standards that should cut emissions through the 2020s. Chemicals that contribute to global warming, like hydrofluorocarbons (used in air conditioners), are being phased out of production nationwide, and so are energy-inefficient household items like incandescent lightbulbs. Also in 2015, solar and wind power provided more than 5 percent of the United States’ electricity for the first time, and construction started on the country’s first offshore wind power project." +
                "Globally, at the United Nations Conference on Climate Change in Paris, 195 countries—including the United States—agreed to pollution-cutting provisions with a goal of preventing the average global temperature from rising more than 1.5 degrees Celsius above preindustrial times. (Scientists say we must stay below a two-degree increase to avoid catastrophic climate impacts.)" +
                "To help make the deal happen, the Obama administration pledged $3 billion to the Green Climate Fund, an international organization dedicated to helping poor countries adopt cleaner energy technologies. Under the terms of the Paris agreement, participating nations will meet every five years, starting in 2020, to revise their plans for cutting CO2 emissions. Beginning in 2023, they will also have to publicly report their progress." +
                "Q: Is global warming too big for me to do anything about it personally?" +
                "A: No. There are many simple steps you can take right now to cut carbon pollution. Make conserving energy a part of your daily routine and your decisions as a consumer. When you shop for new appliances like refrigerators, washers, and dryers, look for products with the government’s Energy Star label; they meet a higher standard for energy efficiency than the minimum federal requirements. When you buy a car, look for one with the highest gas mileage and lowest emissions. You can also reduce your emissions by taking public transportation or carpooling when possible." +
                "And while new federal and state standards are a step in the right direction, much more needs to be done. Voice your support of climate-friendly and climate change preparedness policies, and tell your representatives that transitioning from dirty fossil fuels to clean power should be a top priority—because it’s vital to building healthy, more secure communities.";

        String string2 = "In March a group of Democratic attorneys general formed “AG’s United for Clean Power.”" +
                "It sounds nice enough, doesn’t it? New York Attorney General Eric Schneiderman, standing beside a grinning Al Gore, announced that the gang was going after any energy providers and distributors who may have committed thought crimes such as questioning the human cause of the 0.8oC global warming since 1880." +
                "MIT atmospheric scientist Richard Lindzen said the appropriate response to this tiny warming is to shrug and say, “So what?” It is a tiny fraction of the daily temperature change in most places, and a smaller fraction of the seasonal temperature change. But at the AGs’ meeting Al Gore called it a “climate crisis.”" +
                "The state AGs are in lockstep with federal Attorney General Loretta Lynch, who recently said she is considering legal actions against “climate change deniers” (a nonexistent species, since no one denies that climate changes)." +
                "You may wonder how this could be possible in the United States, where freedom of speech is guaranteed in the Constitution." +
                "Imagine a society in which one is under near-ubiquitous surveillance. Such a vision is remarkably close to the reality of 21st century America, if the words of Supreme Court Justice Steven Breyer are to be believed. In statements regarding a recent case he said, “If you win this case, then there is nothing to prevent the police or the government from monitoring 24 hours a day the public movement of every citizen of the United States.”" +
                "CARTOONS | GARY VARVEL" +
                "VIEW CARTOON " +
                "We’re already under a voluntary microscope with tools like Facebook. Small wonder that politicians desire a similar bonanza. With total data coverage, attorneys general will find it easy to harm those who are not in total support of correct political priorities. Justice Breyer thought such a possibility resembled, too closely for comfort, the dystopian society George Orwell wrote of in his novel 1984." +
                "In more than this we resemble Orwell’s Oceania, which had its own form of twisted English called Newspeak. Newspeak was carefully devised to meet the ideological needs of Ingsoc, or English Socialism. It made it difficult, nearly impossible, for the common prole to have incorrect ideas. Newspeak, as a mode of expression, acted to develop correct mental habits and make all other thoughts inconceivable." +
                "“It was intended,” wrote Orwell, “that when Newspeak had been adopted once and for all and Oldspeak forgotten, a heretical thought, that is, a thought diverging from the principles of Ingsoc, should be literally unthinkable, at least so far as thought is dependent on words.”" +
                "This brings me to global warming. The term occurs frequently in Al Gore’s 1992 book, Earth in the Balance (I stopped counting at 81 times). But when Mr. Gore stood beside the attorneys general there was not even a nostalgic reference to global warming. It has passed out of the memory tubes governed by Newspeak." +
                "The problem with global warming is its precision. The words global and warming have precise and well-understood meanings. Global refers to an all-encompassing entity, in this case geographically specified—the planet Earth. Earth is a medium-sized planet located in space approximately 1.5×1011 m from its closest star. And Earth is warmer, on average, now than it was in the past—well, the late 14th through early 19th centuries, anyway. (It’s also cooler now, on average, than it was in the Holocene Climate Optimum, and probably also the Minoan Warm Period, the Roman Warm Period, and the Medieval Warm Period, but polite conversation doesn’t encompass such inconvenient truths.)" +
                "This is hardly groundbreaking. Our planet has had many periods of warming and cooling." +
                "The AGs insist the direct cause of escalating temperatures is rising atmospheric concentration of CO2—the gas of life—driven by the European and American economic boom post World War II. They warn that warming will become catastrophic unless emissions are stopped." +
                "But the modest warming has stopped while emissions have skyrocketed. This is opposite to the warming computer models predicted. There has been no warming trend from 1997 to the present. (The short-term warming of the first few months of 2016 is due to an unusually, but not unprecedentedly, strong El Niño in the south Pacific and does not constitute a trend.)" +
                "Today those who question the idea of a runaway global warming caused by human CO2 emissions are called deniers. What precisely the deniers deny is never quite specified. That is the point in Newspeak. It is sufficient to wrap words related to rationality and objectivity in the single word denier. Greater precision would be dangerous." +
                "It is the flat line of no warming for almost 20 years that also makes “global warming” unacceptable. It is too precise." +
                "Changing to terms such as Al Gore’s “climate crisis” has the conscious purpose of subtly changing the meaning, by cutting out most of the associations that would otherwise cling to the more precise term. " +
                "Climate crisis and climate change are terms that can be uttered almost without taking thought, whereas global warming is a phrase over which one is obliged to linger at least momentarily. So linger a while before the attorneys general make it a thought crime to hold a different opinion." +
                "  Share this on Facebook (155)" +
                "  Tweet";
        trainAlgorithm(Lists.newArrayList(string1, string2));

        // can't test random so let's print the strings generated with probabilities based on the trained algorithm
        for (int i = 0; i < 80; i++) {
            System.out.println(generateStringWithProbability(40));
        }
    }

    private static class CharProbabilityRange {
        private final double start, end;
        private final char character;

        public CharProbabilityRange(char character, double start, double end) {
            this.character = character;
            this.start = start;
            this.end = end;
        }

        public boolean inRange(double probability) {
            return probability >= start && probability < end;
        }

        @Override
        public String toString() {
            return character + "[" + start + ", " + end + "]";
        }
    }
}