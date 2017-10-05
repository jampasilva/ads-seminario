using Catalogo.Svc.Models.Base;

namespace Catalogo.Svc.Models
{
    public class MercadoriaRepository : RepositoryBase<Mercadoria>
    {
        protected override int GetTablePopulateRowsCount()
        {
            return 50;
        }

        protected override int GetMaxTableRowsCount()
        {
            return 500;
        }

        protected override Mercadoria CreateSample(int identity)
        {
            return new Mercadoria
                {
                    Nome = $"Mercadoria {identity}",
                    Preco = DataGenerator.NextDouble(2, true)
                };
        }
    }
}