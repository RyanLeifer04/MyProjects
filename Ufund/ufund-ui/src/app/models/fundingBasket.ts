import { Needs } from "./needs";

export class FundingBasket {
    items?: Array<FundingBasketItem>;
    userID?: number;
}

export class FundingBasketItem {
    need?: Needs;
    amount?: number;
    checked?: boolean;
}
