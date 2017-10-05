using System;

namespace Catalogo.Svc.Models.Base
{
    public static class DataGenerator
    {
        private static readonly Random DefaulRandom = new Random();


        public static double NextDouble(int? digits = null, bool onlyPositiveValue = false)
        {
            decimal value;
            if(digits == null)
            {
                value = DefaulRandom.NextDecimal();
            }
            else
            {
                var multiply = Convert.ToDecimal(Math.Pow(10, digits.Value));

                value = Math.Truncate((decimal)(DefaulRandom.NextDouble() * 100)*multiply)/100m;
            }

            return (double)(onlyPositiveValue ? Math.Abs(value) : value);
        }

        public static int NextInt32(this Random rng)
        {
            unchecked
            {
                var firstBits = rng.Next(0, 1 << 4) << 28;
                var lastBits = rng.Next(0, 1 << 28);

                return firstBits | lastBits;
            }
        }

        public static decimal NextDecimal(this Random rng)
        {
            var scale = (byte)rng.Next(29);
            var sign = rng.Next(2) == 1;

            return new decimal(
                rng.NextInt32(), rng.NextInt32(), rng.NextInt32(), sign, scale);
        }
    }
}