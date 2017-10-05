using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using Catalogo.Svc.Models;
using Microsoft.AspNetCore.Mvc;

// For more information on enabling Web API for empty projects, visit http://go.microsoft.com/fwlink/?LinkID=397860

namespace Catalogo.Svc.Controllers
{
	[Route("api/[controller]")]
	public class MercadoriaController : Controller
	{
		private readonly MercadoriaRepository _repository;

		public MercadoriaController()
		{
			_repository = new MercadoriaRepository();
		}

		[HttpGet]
		public IEnumerable<Mercadoria> Get()
		{
			Thread.Sleep(1500);

			return _repository.GetAll();
		}

		[HttpGet("{id}")]
		public Mercadoria Get(int id)
		{
			return _repository.Get(id);
		}

		[HttpPost]
		public Object Post([FromBody]Mercadoria value)
		{
			_repository.Insert(value);

			return new
				{
					Identity = value.ID
				};
		}

		[HttpPut("{id}")]
		public bool Put(int id, [FromBody]Mercadoria value)
		{
			return _repository.Update(id, value);
		}

		[HttpDelete("{id}")]
		public bool Delete(int id)
		{
			return _repository.Delete(id);
		}
	}
}
